package server;

import Client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static int clientNum = 0;
    private static List<Socket> clients;
    public static void main(String[] args) {
        int port = 6666; // 1025 - 65535

        try(ServerSocket server = new ServerSocket(port)) {

            System.out.println("Waiting for a client...");

            Socket socket = server.accept();
            System.out.println("Got a client.");


            BufferedReader clientSays = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String text;
            while ((text = clientSays.readLine()) != null) {

                System.out.println(text);
                String temp = keyboard.readLine();
                output.println("Server: " + temp);
            }
            keyboard.close();
            clientSays.close();
            output.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
