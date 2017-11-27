package client.console;

import java.io.*;
import java.net.SocketException;
import java.time.LocalTime;

import client.base.ClientBase;
import client.base.ClientType;
import message.Message;
import message.MessageType;

public class ConsoleClient extends ClientBase {
    public final ClientType type = ClientType.CONSOLE;
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
            /*
                Message will not be sent until the string is not empty
             */
            if (text.equals("")) {
                continue;
            }
            /*
                In case of someone leaves the chat,
                all the rest clients receive a message
                with type OFFLINE with text "exit"
                which will not be printed
             */
            if (text.equals("exit")) {
                try {
                    out2.writeObject(new Message(MessageType.OFFLINE, super.name, "exit", LocalTime.now(), super.socket.getPort()));
                    receiver.setStop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            /*
                Ordinary messages is sent with type MESSAGE
             */
            Message newMessage = new Message(MessageType.MESSAGE, super.name, text, LocalTime.now(), super.socket.getPort());

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
//                    if (name == null) {
//                        continue;
//                    }
                    msg = (Message)in2.readObject();
                    /*
                        In the case of message type is SERVER_MESSAGE, print it with just text
                     */
                    if (msg.getType() == MessageType.SERVER_MESSAGE) {
                        System.out.println(msg.getSentTime() + " " + msg.getText());
                        continue;
                    }
                    System.out.println(msg.getSentTime()+ " " + msg.getSenderName() + ": " + msg.getText());

                }
            } catch (SocketException e) {
                /*
                 *  If a SocketException has been caught, countdown from 3 to 1
                 */
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
