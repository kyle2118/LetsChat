package client.base;

import message.Message;
import message.MessageType;
import res.Const;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;

public class ClientBase {
    protected String name;

    protected Socket socket;
    protected ObjectOutputStream out2;
    protected ObjectInputStream in2;

    protected Scanner keyboard;

    protected ClientBase() {
//        try {
//
//
//            /// TODO ClientBase shouldn't sent client type, since in the future GUI client will send another type
//            /// TODO Enter name not here, GUI client does not have console window!!!.
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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


}
