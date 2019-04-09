package controllers;

import javafx.event.ActionEvent;
import models.game.GameInfo;

import java.io.IOException;

public class HomeController extends Controller {

    public void opponent_start_ttt(ActionEvent event) throws IOException {
        new OpponentController().show(event, "opponent", GameInfo.TICTACTOE);
    }

    public void opponent_start_othello(ActionEvent event) throws IOException {
        new OpponentController().show(event, "opponent", GameInfo.REVERSI);
    }

}
