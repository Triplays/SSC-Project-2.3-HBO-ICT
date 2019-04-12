package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Views/start.fxml"));

        primaryStage.setTitle("Game");
        primaryStage.setMinWidth(825);
        primaryStage.setMinHeight(590);

        Scene scene = new Scene(root, 825, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
