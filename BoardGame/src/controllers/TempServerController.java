package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.User;
import models.game.GameInfo;
import models.gamecontroller.ServerGameController;

import java.io.IOException;

public class TempServerController extends Controller {
    private static GameInfo game_type;
    private static ServerGameController controller;

    public void show(ActionEvent event, String view, GameInfo game) throws IOException {
        game_type = game;
        Stage stage = get_stage(event);
        stage.setScene(new_scene(view, event));

        // TODO: User puts in IP and port
        controller = new ServerGameController("localhost", 7789, game_type, User.get_username());

        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane listPane = (AnchorPane) stage.getScene().lookup("#list");
        AnchorPane boardPane = (AnchorPane) stage.getScene().lookup("#board");

        listPane.getChildren().add(controller.getServerView());
        boardPane.getChildren().add(controller.getDisplay());
    }

    public void refresh(ActionEvent event){
        controller.updatePlayerList();
    }

    public void back(ActionEvent event) throws IOException{
        controller.closeController();
        get_stage(event).setScene(new_scene("opponent", event));
    }
}
