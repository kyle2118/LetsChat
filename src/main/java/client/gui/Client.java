package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

//        String fxml = "/fxml/client_gui.fxml";
//        FXMLLoader loader = new FXMLLoader();
//        Parent root = (Parent)loader.load(getClass().getResourceAsStream(fxml));

        AnchorPane anchor = new AnchorPane();
        anchor.setPrefSize(752.0, 532.0);
        anchor.setStyle("-fx-background-color: #778899;");


        Button buttonSend = new Button("Send");
        buttonSend.setFont(new Font(15));
        buttonSend.setLayoutX(524.0);
        buttonSend.setLayoutY(470);
        buttonSend.setPrefSize(70, 17);
        buttonSend.setStyle("-fx-background-radius: 20px");

        Button buttonExit = new Button("Exit");
        buttonExit.setFont(new Font(15));
        buttonExit.setLayoutX(640.0);
        buttonExit.setLayoutY(470);
        buttonExit.setPrefSize(70, 17);
        buttonExit.setStyle("-fx-background-radius: 20px");

        anchor.getChildren().addAll(buttonExit, buttonSend);

        primaryStage.setTitle("LetsChat");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(anchor));

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
