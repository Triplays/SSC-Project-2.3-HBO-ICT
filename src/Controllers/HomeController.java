package Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;

public class HomeController extends Controller {

    public void othello_start(ActionEvent event) throws IOException {
        get_stage(event).setScene(new_scene("othello"));
    }

}
