package message;

import client.base.ClientType;

import java.io.Serializable;
import java.time.LocalTime;

public class Message implements Serializable {
    private byte version = 1;
    private MessageType type;
    private String senderName;
    private String text;
    private LocalTime sentTime;
    private int port;
    /*
        Constructors
     */
    public Message(MessageType type, String name, String text, LocalTime sentTime, int port) {
        this.type = type;
        this.senderName = name;
        this.text = text;
        this.sentTime = sentTime;
        this.port = port;
    }
    public Message(Message message) {
        this(message.type, message.senderName, message.text, message.sentTime, message.port);
    }
    /*
        Methods. Setters & Getters
     */
    public MessageType getType() {
        return type;
    }

    public String getSenderName() {
        return senderName;
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
