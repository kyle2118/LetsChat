package gui.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ControllerClient {
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
        buttonSend.setText("Changed");
        System.out.println(input.getText() == null);
        System.out.println(msg.getText() == null);
//        String text = input.getText();
//        if (text == null) {
//            return;
//        }
//        Label message = new Label(text);
//
//        message.setPrefSize(100, 30);
//        message.setScaleX(xStart);
//        message.setScaleY(yStart);
//        yStart += 50;
//        historyField.getChildren().add(message);

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
