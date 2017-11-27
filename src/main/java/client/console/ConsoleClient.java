package client.console;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.Scanner;

import client.base.ClientBase;
import message.Message;
import res.Const;

public class ConsoleClient extends ClientBase {
    public static void main(String[] args) throws  InterruptedException {
        new ConsoleClient();
    }

    public ConsoleClient() {

        /*
           Start an inner class which will work independently receiving messages from another participants
           A participant does not receive messages until he/she inputs name
         */

        Receiver receiver = new Receiver();
        receiver.start();

        /*
         * Loop works until a participant would like to finish the conversation
         */
        String text;
        while (!socket.isClosed()) {
            text = keyboard.nextLine();
            if (text.equals("exit")) {
                try {
                    out2.writeObject(new Message(super.name, "exit", LocalTime.now(), super.socket.getPort()));
                    receiver.setStop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            if (text.equals("")) {
                continue;
            }
            Message newMessage = new Message(super.name, text, LocalTime.now(), super.socket.getPort());

            try {
                out2.writeObject(newMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        close();
    }

    /*
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
        private void setStop() {
            this.stopped = true;
        }

        /*
         * Sets time of online.
         * Prints everything from Console.ConsoleClient.client to console.
         */
        @Override
        public void run() {
            Message msg;
            try {
                while (!stopped) {
                    msg = (Message)in2.readObject();
                    if (msg.getName().equals("Server")) {
                        System.out.println(msg.getSentTime() + " " + msg.getText());
                        continue;
                    }
                    System.out.println(msg.getSentTime()+ " " + msg.getName() + ": " + msg.getText());
                }
            } catch (SocketException e) {
                System.out.println("Good bye!");
                for (int i = 3; i >= 1; i--) {
                    System.out.print(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    System.out.print("\r");
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }
    }
}
