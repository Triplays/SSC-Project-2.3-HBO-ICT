package controllers;


import javafx.event.ActionEvent;

import java.io.IOException;

public class OponentController extends Controller {
    private static String game_type;

    public void show(ActionEvent event, String view, String game) throws IOException {
        game_type = game;
        get_stage(event).setScene(new_scene(view, event));
    }

    public void game_start(ActionEvent event) {
        System.out.println(game_type);
        if (game_type == "othello") {
            new OthelloController().show(event);
        } else {
            new OthelloController().show(event);
        }
    }

    public void back(ActionEvent event) throws IOException{
        get_stage(event).setScene(new_scene("home", event));
    }
}
