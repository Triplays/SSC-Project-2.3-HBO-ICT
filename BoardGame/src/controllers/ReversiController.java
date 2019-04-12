package controllers;

import javafx.scene.Scene;
import models.User;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.gamecontroller.LocalGameController;
import models.game.Field;
import models.game.GameInfo;

import java.io.IOException;

public class ReversiController extends Controller
{

    void show(ActionEvent event)
    {
        Stage stage = get_stage(event);
        try {
            stage.setScene(new_scene("othello", event));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, User.get_username(), Field.BLACK, 5);
        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#board");

        pane.getChildren().add(controller.getDisplay());

        System.out.println("username: " + User.get_username());
    }

    public void home_start(ActionEvent event) throws IOException
    {
        get_stage(event).setScene(new_scene("home", event));
    }

    public void board() {
        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, User.get_username(), Field.BLACK, 5);
        Thread thread = new Thread(controller);
        thread.start();
        Scene scene = new Scene(controller.getDisplay());
    }

}
