package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
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
    }

    public void start(ActionEvent event) {
        Stage stage = get_stage(event);

        TextField ipadresField = (TextField) get_stage(event).getScene().lookup("#ipadres");
        TextField portField = (TextField) get_stage(event).getScene().lookup("#port");

        String targetServer = ipadresField.getText();
        int port = Integer.valueOf(portField.getText());

        controller = new ServerGameController(targetServer, port, game_type, User.get_username());

        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane listPane = (AnchorPane) stage.getScene().lookup("#list");
        AnchorPane boardPane = (AnchorPane) stage.getScene().lookup("#board");

        listPane.getChildren().add(controller.getServerView());
        boardPane.getChildren().add(controller.getDisplay());

        remove_item("main", "ippane", stage);
    }

    public void refresh(ActionEvent event){
        controller.updatePlayerList();
    }

    public void back(ActionEvent event) throws IOException{
        controller.closeController();
        get_stage(event).setScene(new_scene("opponent", event));
    }
}
