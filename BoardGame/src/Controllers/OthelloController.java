package Controllers;

import javafx.event.ActionEvent;
import javafx.scene.Scene;

import java.io.IOException;

public class OthelloController extends Controller {

    public void home_start(ActionEvent event) throws IOException {
        get_stage(event).setScene(new_scene("home"));
    }


    public void board() {
        controller.LocalVersusGameContoller controller = new controller.LocalVersusGameContoller();
        Thread thread = new Thread(controller);
        thread.start();
        Scene scene = new Scene(controller.getGame().getDisplay().getWrapperPane());
    }

}
