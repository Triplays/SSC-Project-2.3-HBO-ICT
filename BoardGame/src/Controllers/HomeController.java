package Controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController extends Controller {

    public void othello_start(ActionEvent event) throws IOException {
        Stage stage = get_stage(event);
        stage.setScene(new_scene("othello"));

        controller.LocalVersusGameContoller controller = new controller.LocalVersusGameContoller();
        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#board");

        pane.getChildren().add(controller.getGame().getDisplay().getWrapperPane());
    }

}
