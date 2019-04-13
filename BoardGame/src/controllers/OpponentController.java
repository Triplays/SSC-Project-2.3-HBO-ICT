package controllers;


import javafx.event.ActionEvent;
import models.game.GameInfo;
import models.game.Opponent;

import java.io.IOException;

public class OpponentController extends Controller {
    private static GameInfo game_type;

    public void show(ActionEvent event, String view, GameInfo game) throws IOException {
        game_type = game;
        get_stage(event).setScene(new_scene(view, event));
    }

    public void pvai(ActionEvent event) throws IOException {
        System.out.println(game_type);
        new GUIGameController().show(event, game_type, Opponent.AI);
    }

    public void pvp(ActionEvent event) throws IOException {
        System.out.println(game_type);
        new GUIGameController().show(event, game_type, Opponent.Human);
    }

    public void back(ActionEvent event) throws IOException{
        get_stage(event).setScene(new_scene("home", event));
    }
}
