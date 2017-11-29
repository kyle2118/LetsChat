package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import client.base.ClientType;
import message.Message;
import message.MessageType;
import res.Const;

public class Server {

    private static volatile int usersNum = 0;

    private List<Connection> clients = Collections.synchronizedList(new ArrayList<>());
    // History of last n messages, currently n = 10
    private LinkedList<Message> messageHistory = new LinkedList<>();
    private ServerSocket serverSocket;

    /*
        Enter point for server.Server() class
     */
    public static void main(String[] args) {

        new Server();
    }

    private Server() {
        //Creates a commander which allows server to give commands
        Commander commander = new Commander();
        commander.start();
        try {
            serverSocket = new ServerSocket(Const.PORT);
            // Server accepts new connections until commander thread is alive or socket is open
            while (!serverSocket.isClosed()) {
                /*
                    Connection is a User bean. But complicated with socket, output(input) streams and it's a thread
                    In case of a new connection, ex. new user connects to server
                    it is added to the list of connection and that connection thread starts
                 */
                if (!commander.isAlive()) {
                    closeAll();
                    System.exit(0);
                }
                Socket socket = serverSocket.accept();
                usersNum++;
                Connection connection = new Connection(socket);
                connection.start();
                clients.add(connection);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }
    private void closeAll() {
        try {
            serverSocket.close();

            synchronized (clients) {
                for (Connection client : clients) {
                    client.close();
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private class Commander extends Thread {
        public Scanner scanner;
        public Commander() {
            scanner = new Scanner(System.in);
        }
        @Override
        public void run() {
            String command;
            do {
                command = scanner.nextLine();
                if (command.equals("print")) {
                    synchronized(clients) {
                        if (clients.isEmpty()) {
                            System.out.println("No connections");
                            continue;
                        }
                        System.out.println("+----------+----------+--------+-------------+");
                        System.out.printf("|%-10s|%-10s|%-8s|%-24s|\n", "name", "status", "type", "connect time");
                        for (Connection client : clients) {
                            System.out.println(client.toString());
                        }
                        System.out.println("+----------+----------+--------+-------------+");
                    }
                }
                if (command.equals("history")) {
                    for (Message msg : messageHistory) {
                        System.out.println(msg.getSentTime()+ " " + msg.getSenderName() + ": " + msg.getText());
                    }
                }
            } while (!command.equals("shutdown"));

        }
    }
    private class Connection extends Thread {
        private ObjectInputStream in2;
        private ObjectOutputStream out2;
        private Socket socket;

        private String name;
        private String status;
        private ClientType type;
        private LocalDateTime connectTime;
        private LocalDateTime disconnectTime;


        private Connection(Socket socket) {
            this.socket = socket;
            try {
                in2 = new ObjectInputStream(socket.getInputStream());
                out2 = new ObjectOutputStream(socket.getOutputStream());
                connectTime = LocalDateTime.now();
                out2.writeObject(new Message(MessageType.SERVER_MESSAGE, "Server", "Hello, welcome to out chat", LocalTime.now(), 6030));
                for (Message msg : messageHistory) {
                    out2.writeObject(msg);
                }

            } catch (IOException e ) {
                e.printStackTrace();
                close();
            }
        }
        @Override
        public void run() {
            try {
                //name = in.readLine();
                /*
                 *  To successful connection, clients sends two objects:
                 *  1. Client type
                 *  2. Message with name of new client's name
                 */
                type = (ClientType)in2.readObject();
                Message newUserName = (Message)in2.readObject();
                name = newUserName.getSenderName();

                sendAllClients(new Message(
                        MessageType.SERVER_MESSAGE,
                        "Server",
                        newUserName.getSenderName() + " is online.",
                        newUserName.getSentTime(),
                        newUserName.getPort()));

                System.out.println(name + " is online.");

                /*
                 * Server expects messages from a client until socket is open or it sends a text 'exit'
                 * After receiving ordinary message from a client, server sends that msg to everyone
                 * iterating the list of connected users.
                 */
                Message msg;
                while (!socket.isClosed()) {
                    msg = (Message) in2.readObject();
                    if (msg.getText().equals("exit")) {
                        sendAllClients(new Message(
                                MessageType.SERVER_MESSAGE,
                                "Server",
                                msg.getSenderName() + " is offline.",
                                msg.getSentTime(),
                                msg.getPort()));
                        System.out.println(msg.getSenderName() + " is offline.");
                        break;
                    }
                    /*
                        Keeps the last 10 messages,
                        FIFO type list
                     */
                    if (messageHistory.size() >= 10) {
                        messageHistory.removeFirst();
                    }
                    messageHistory.addLast(msg);

                    sendAllClients(msg);
                    System.out.println(msg.getSentTime()+ " " + msg.getSenderName() + ": " + msg.getText());
                }



            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        /*
         * Iterates a concurrent list and sends everyone a message received as a parameter.
         * Excludes 'this' object, since there is no reason to send itself
         */
        private synchronized void sendAllClients(Message message) throws IOException {
            for (Connection current : clients) {
                if (current.equals(Thread.currentThread()))
                    continue;
                current.out2.writeObject(message);
            }
        }
        @Override
        public String toString() {
            String client = String.format("|%-10s|%-10s|%-8s|%-24s|",
                                          name, status, type.toString().toLowerCase(), connectTime);
            return client;
        }
        private void close() {
            try {
                disconnectTime = LocalDateTime.now();
                in2.close();
                out2.close();
                socket.close();

                // Delete current object from the list of online users
                clients.remove(this);
                usersNum--;
//                if (clients.isEmpty()) {
//                    Server.this.closeAll();
//                    System.exit(0);
//                }

            } catch (IOException e) {
                System.out.println("Unable to close resources");
            }
        }

    }
}
