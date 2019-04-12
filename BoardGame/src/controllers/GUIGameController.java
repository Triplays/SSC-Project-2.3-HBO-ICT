package controllers;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.User;
import models.game.Field;
import models.game.GameInfo;
import models.game.Opponent;
import models.gamecontroller.LocalGameController;

import java.io.IOException;

public class GUIGameController extends Controller{
    private static GameInfo last_game_type;
    private static Opponent last_opponent;

    void show(ActionEvent event, GameInfo game_type,Opponent opponent) {
        last_game_type = game_type;
        last_opponent = opponent;

        Stage stage = get_stage(event);
        try {
            switch (game_type){
                case REVERSI:
                    stage.setScene(new_scene("othello", event));
                    break;
                case TICTACTOE:
                    stage.setScene(new_scene("tictactoe", event));
                    break;
                default:
                    //needs exception
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalGameController controller;

        if(opponent == Opponent.AI){
            controller = new LocalGameController(game_type, User.get_username(), Field.BLACK, 5);
        }
        else{
            controller = new LocalGameController(game_type, User.get_username(), Field.BLACK, "Speler 2");
        }
        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#board");

        pane.getChildren().add(controller.getDisplay());

        System.out.println("Username: " + User.get_username());
        System.out.println("Opponent: " + controller.getOpponentname());
    }


    public void opgeven(ActionEvent event) throws IOException {
        get_stage(event).setScene(new_scene("opponent", event));
    }

    public void reset(ActionEvent event) throws IOException{
        new GUIGameController().show(event, last_game_type, last_opponent);
    }

}