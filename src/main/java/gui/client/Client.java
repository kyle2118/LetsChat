package gui.client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Client extends Application {
    @FXML
    Button button;
    @Override
    public void start(Stage primaryStage) throws Exception {

        String fxml = "/fxml/gui.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent)loader.load(getClass().getResourceAsStream(fxml));


        primaryStage.setTitle("LetsChat");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
