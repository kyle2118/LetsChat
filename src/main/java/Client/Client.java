package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;

public class Client {
    private String name;
    private String password;
    private LocalDateTime connectedTime;
    private LocalDateTime disconnectTime;

    private static int serverPort;
    private static String address;
    static {
        serverPort = 6666;     // Port server set on
        address = "127.0.0.1";  // Localhost ip address
    }

    public Client() {

    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            InetAddress ipAddress = InetAddress.getByName(address); // Object representing our ip
            Socket serverSocket = new Socket(ipAddress, serverPort);  // Creating a socket using ip and server port

            BufferedReader serverSays = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            PrintWriter output = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Your name: ");
            client.setName(keyboard.readLine());
            String text;

            while ((text = keyboard.readLine()) != null) {
                if (text.equalsIgnoreCase("exit"))
                    break;
                output.println(client.name + ": " + text);
//                writer.flush();
                text = serverSays.readLine();
                System.out.println(text);
            }
            keyboard.close();
            output.close();
            serverSays.close();
            serverSocket.close();
        } catch (Exception e) {
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
}
