package client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import res.Const;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ControllerClient {
    private int labelX = 15, labelY = 15;
    private String name;
    private Color color;

    @FXML
    private Button buttonSend;
    @FXML
    private Button buttonExit;
    @FXML
    private TextField input;
    @FXML
    private AnchorPane historyField;


    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    public ControllerClient() {
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket(Const.ADDRESS, Const.PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            System.out.println("Your name: ");
            name = scanner.nextLine();
            out.println(name);

            Receiver receiver = new Receiver();
            receiver.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void send() {

        String text = input.getText();
        if (text == null) {
            System.out.println("text from input == null");
            return;
        }
        Label message = new Label(text);

        message.setPrefSize(100, 30);
        message.setFont(new Font(20));
        message.setLayoutX(labelX);
        message.setLayoutY(labelY);
        labelY += 30;
        historyField.getChildren().add(message);
        input.setText("");
    }
    @FXML
    public void destroy() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Receiver extends Thread {
        private boolean stopped = false;
        private void setStop() { stopped = false; }
        @Override
        public void run() {
            System.out.println("started receiving messages");
            try {
                while (!stopped) {
                    String msg = in.readLine();
                    Label label = new Label(msg);
                    label.setLayoutX(labelX);
                    label.setLayoutX(labelY);
                    labelY += 30;
                    historyField.getChildren().add(label);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
