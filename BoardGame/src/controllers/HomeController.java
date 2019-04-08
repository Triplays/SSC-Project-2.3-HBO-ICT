package controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class HomeController extends Controller {

    public void opponent_start_ttt(ActionEvent event) throws IOException {
        new OponentController().show(event, "opponent", "ttt");
    }

    public void opponent_start_othello(ActionEvent event) throws IOException {
        new OponentController().show(event, "opponent", "othello");
    }

}
