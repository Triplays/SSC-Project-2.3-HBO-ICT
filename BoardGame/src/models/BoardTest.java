package models;

import models.controller.LocalComputerGameController;
import models.controller.LocalShowdownGameController;
import models.controller.LocalVersusGameContoller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BoardTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //LocalVersusGameContoller controller = new LocalVersusGameContoller();
        //LocalComputerGameController controller = new LocalComputerGameController();
        LocalShowdownGameController controller = new LocalShowdownGameController();
        Thread thread = new Thread(controller);
        thread.start();
        Scene scene = new Scene(controller.getGame().getDisplay().getWrapperPane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
