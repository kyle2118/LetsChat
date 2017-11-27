package message;

import java.io.Serializable;
import java.time.LocalTime;

public class Message implements Serializable {
    private byte version = 1;
    private String name;
    private String text;
    private LocalTime sentTime;
    private int port;
    public Message(String name, String text, LocalTime sentTime, int ip) {
        this.name = name;
        this.text = text;
        this.sentTime = sentTime;
        this.port = ip;
    }
    public Message(Message message) {
        this(message.name, message.text, message.sentTime, message.port);
    }
    public String getName() {
        return name;
    }
    public String getText() {
        return text;
    }
    public LocalTime getSentTime() {
        return sentTime;
    }
    public int getPort() {
        return port;
    }
}
