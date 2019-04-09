package controllers;


import javafx.event.ActionEvent;
import models.game.GameInfo;

import java.io.IOException;

public class OpponentController extends Controller {
    private static GameInfo game_type;

    public void show(ActionEvent event, String view, GameInfo game) throws IOException {
        game_type = game;
        get_stage(event).setScene(new_scene(view, event));
    }

    public void game_start(ActionEvent event) {
        System.out.println(game_type);
        switch (game_type){
            case REVERSI:
                new OthelloController().show(event);
                break;
            case TICTACTOE:
                new TicTacToeController().show(event);
                break;
            default:
                //needs exception
                break;

        }
    }

    public void back(ActionEvent event) throws IOException{
        get_stage(event).setScene(new_scene("home", event));
    }
}
