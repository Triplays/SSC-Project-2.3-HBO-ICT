package controllers;

import models.User;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.controller.LocalGameController;
import models.game.GameInfo;

import java.io.IOException;

public class ReversiController extends Controller
{

    void show(ActionEvent event)
    {
        Stage stage = get_stage(event);
        try {
            stage.setScene(new_scene("othello"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalGameController controller = new LocalGameController(GameInfo.REVERSI);
        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#board");

        pane.getChildren().add(controller.getGame().getDisplay().getWrapperPane());

        System.out.println("username: " + User.get_username());
    }

    public void home_start(ActionEvent event) throws IOException
    {
        get_stage(event).setScene(new_scene("home"));
    }

}
