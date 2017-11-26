package client.console;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;

import client.base.ClientBase;
import message.Message;
import res.Const;

public class ConsoleClient extends ClientBase {
    public static void main(String[] args) {
        new ConsoleClient();
    }

    public ConsoleClient() {

        /**
         *  Start an inner class which will work independently receiving messages from another participants
         *  A participant does not receive messages until he/she inputs name
         */

        Receiver receiver = new Receiver();
        receiver.start();

        /**
         * Loop works until a participant would like to finish the conversation
         */
        String text = "";
        while (!text.equals("exit") || !socket.isClosed()) {
            text = keyboard.nextLine();
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
        receiver.setStop();
        close();
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
