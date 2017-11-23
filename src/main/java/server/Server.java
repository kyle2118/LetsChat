package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import res.Const;

public class Server {

    private static volatile int usersNum = 0;

    private List<Connection> clients = Collections.synchronizedList(new ArrayList<>());
    private ServerSocket serverSocket;

    /*
        Enter point for server class
     */
    public static void main(String[] args) {
        new Server();
    }

    private Server() {
        Scanner scanner = new Scanner(System.in);
        try {
            serverSocket = new ServerSocket(Const.PORT);
            while (true) {
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
    private class Connection extends Thread {
        private BufferedReader in;
        private PrintWriter out;
        private Socket socket;

        private String name = "";

        private Connection(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e ) {
                e.printStackTrace();
                close();
            }
        }
        @Override
        public void run() {
            try {
                name = in.readLine();
                sendAllClients(name + " is online.");
                System.out.println(name + " is online.");
                String text;
                while (true) {
                    text = in.readLine();
                    if (text.equals("exit"))
                        break;

                    sendAllClients(name + ": " + text);
                    System.out.println(name + ": " + text);
                }

                sendAllClients(name + " is offline.");
                System.out.println(name + " is offline.");


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        private synchronized void sendAllClients(String message) {
            for (Connection current : clients) {
                if (current.equals(Thread.currentThread()))
                    continue;
                current.out.println(message);
            }
        }
        private void close() {
            try {
                in.close();
                out.close();
                socket.close();

                // Delete current object from the list of online users
                clients.remove(this);
                usersNum--;
                if (clients.isEmpty()) {
                    Server.this.closeAll();
                    System.exit(0);
                }

            } catch (IOException e) {
                System.out.println("Unable to close resources");
            }
        }

    }
}
