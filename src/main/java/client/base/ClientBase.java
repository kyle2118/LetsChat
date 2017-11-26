package client.base;

import res.Const;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientBase {
    protected String name;
    protected LocalDateTime connectTime;
    protected LocalDateTime disconnectTime;

    protected Socket socket;
    protected PrintWriter out;
    protected ObjectOutputStream out2;
    protected BufferedReader in;
    protected ObjectInputStream in2;

    protected ClientBase() {
        try {
            socket = new Socket(Const.ADDRESS, Const.PORT);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out2 = new ObjectOutputStream(socket.getOutputStream());
            in2 = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void close() {
        try {
            in.close();
            out.close();
            socket.close();
            in2.close();
            out2.close();
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
