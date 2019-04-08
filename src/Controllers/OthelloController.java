package Controllers;

import javafx.event.ActionEvent;

import java.io.IOException;

public class OthelloController extends Controller {

    public void home_start(ActionEvent event) throws IOException {
        get_stage(event).setScene(new_scene("home"));
    }

}
