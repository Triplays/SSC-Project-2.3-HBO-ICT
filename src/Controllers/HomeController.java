package Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Views/home.fxml"));
        primaryStage.setTitle("Start");

        Scene scene = new Scene(root, 826, 551);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
