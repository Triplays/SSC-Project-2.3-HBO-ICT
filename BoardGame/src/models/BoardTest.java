package models;

import models.gamecontroller.LocalGameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.game.GameInfo;

public class BoardTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, 6, 3);
        Thread thread = new Thread(controller);
        thread.start();

        Scene scene = new Scene(controller.getDisplay());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
