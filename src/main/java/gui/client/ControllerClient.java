package gui.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class ControllerClient {
    private static int labelX, labelY;
    private String name;
    private Color color;

    private static int xStart, yStart;
    @FXML
    private Button buttonSend;
    @FXML
    private TextField input;
    @FXML
    private AnchorPane historyField;
    @FXML
    private Label msg;

    @FXML
    public void send() {

        String text = input.getText();
        if (text == null) {
            System.out.println("text from input == null");
            return;
        }
        Label message = new Label(text);
        System.out.println(message.getText());
        message.setPrefSize(100, 30);
//        yStart += 50;
        message.setLayoutX(msg.getLayoutX());
        message.setLayoutY(msg.getLayoutY() + 60);
        historyField.getChildren().add(message);


    }

//
//    /**
//     * in case of a client wants to send a empty message, button is disabled
//     */
//    @FXML
//    public void enableButton() {
//        buttonSend.setDisable(false);
//    }


}
