package server;

import Client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import res.Const;

public class Server {

    private static int usersNum = 0;

    private List<Connection> clients = Collections.synchronizedList(new ArrayList<>());
    private ServerSocket serverSocket;

    /*
        Enter point for server class
     */
    public static void main(String[] args) {
        new Server();
    }

    public Server() {
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
                Iterator iterator = clients.iterator();
                while (iterator.hasNext()) {
                    ((Connection)iterator.next()).close();
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

        public Connection(Socket socket) {
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
                String text = "";
                while (true) {
                    text = in.readLine();
                    if (!text.equals("exit"))
                        break;

                    sendAllClients(name + ": " + text);
                }

                sendAllClients(name + " is offline.");


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        private synchronized void sendAllClients(String message) {
            Iterator<Connection> it = clients.iterator();
            while (it.hasNext()) {
                it.next().out.println(name + ": " + message);
            }
        }
        public void close() {
            try {
                in.close();
                out.close();
                socket.close();

                // Delete current object from the list of online users
                clients.remove(this);
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
