package Client;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

import res.Const;

public class Client {
    private String name;
    private String password;
    private LocalDateTime connectedTime;
    private LocalDateTime disconnectTime;

    private static BufferedReader in;
    private static PrintWriter out;

    private Socket socket;

    /*
        Enter Point for Client
     */
    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket(Const.ADDRESS, Const.PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Your name: ");
            name = scanner.nextLine();
            out.println(name);

            Receiver receiver = new Receiver();
            receiver.start();

            String text = "";
            while (!text.equals("exit")) {
                text = scanner.nextLine();
                out.println(text);
            }
            receiver.setStop();


        } catch (IOException e) {
            System.err.println("Unable to create a Socket using ip address:" + Const.ADDRESS + " and port: " + Const.PORT);
        } finally {
            close();
        }

    }
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getConnectedTime() {
            return connectedTime;
    }

    public LocalDateTime getDisconnectTime() {
        return disconnectTime;
    }

    /**
     *  Thread printing everything that server sends.
     *  Does not participate in logic of typing a message.
     *  Thread resolves a problem when each side of chat must
     *  hit the 'enter' to receive data sent.
     */
    private class Receiver extends Thread {

        private boolean stopped = false;
        /*
            Stops receiving messages
         */
        public void setStop() {
            this.stopped = true;
        }

        /**
         * Sets time of online.
         * Prints everything from server to console.
         */
        @Override
        public void run() {
            connectedTime = LocalDateTime.now();
            try {
                while (!stopped) {
                    String text = in.readLine();
                    System.out.println(text);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
