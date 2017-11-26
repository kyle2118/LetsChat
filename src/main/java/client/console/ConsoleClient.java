package client.console;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;

import client.base.ClientBase;
import message.Message;
import res.Const;

public class ConsoleClient extends ClientBase {
    private Scanner scanner = null;
    public static void main(String[] args) {
        new ConsoleClient();
    }

    public ConsoleClient() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Your name: ");
        setName(scanner.nextLine());
        try {
            out2.writeObject(new Message(name, "online", LocalTime.now(), 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Receiver receiver = new Receiver();
        receiver.start();

        String text = "";
        while (!text.equals("exit")) {
            text = scanner.nextLine();
            LocalTime sentTime = LocalTime.now();
            Message newMessage = new Message(super.name, text, sentTime, super.socket.getPort());

            try {
                out2.writeObject(newMessage);
                System.out.println("Object sent via serializable message");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        receiver.setStop();
    }

    /**
     *  Thread printing everything that Console.ConsoleClient.client sends.
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
         * Prints everything from Console.ConsoleClient.client to console.
         */
        @Override
        public void run() {
            Message msg;
            try {
                while (!stopped) {
//                    String text = in.readLine();
                    msg = (Message)in2.readObject();
                    System.out.println(msg.getSentTime()+ " " + msg.getName() + ": " + msg.getText());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException er) {
                er.printStackTrace();
            }
        }
    }
}
