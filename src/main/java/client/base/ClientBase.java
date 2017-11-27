package client.base;

import message.Message;
import res.Const;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class ClientBase {
    protected String name;
    private LocalDateTime connectTime;
    private LocalDateTime disconnectTime;

    protected Socket socket;
    protected ObjectOutputStream out2;
    protected ObjectInputStream in2;

    protected Scanner keyboard;

    protected ClientBase() {
        try {
            socket = new Socket(Const.ADDRESS, Const.PORT);
            out2 = new ObjectOutputStream(socket.getOutputStream());
            in2 = new ObjectInputStream(socket.getInputStream());

            keyboard = new Scanner(System.in);

            System.out.println("Your name: ");
            setName(keyboard.nextLine());
            out2.writeObject(new Message(name, "online", LocalTime.now(), 1234));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void close() {
        try {
            in2.close();
            out2.close();
            socket.close();
        } catch(IOException e) {
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
        return connectTime;
    }

    public LocalDateTime getDisconnectTime() {
        return disconnectTime;
    }

}
