// Author: Przemysław Sałęga

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        GridPane root = fxmlLoader.load(new FileInputStream(System.getProperty("user.dir") + "\\src\\gui\\sample.fxml"));
        primaryStage.setTitle("Cipher");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(gui.Controller.class.getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
