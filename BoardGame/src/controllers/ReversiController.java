package controllers;

import javafx.event.Event;
import javafx.scene.control.Button;
import models.User;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.gamecontroller.LocalGameController;
import models.game.Field;
import models.game.GameInfo;

import java.io.IOException;

public class ReversiController extends Controller {
    private Field color;
    private int strength;

    void show(ActionEvent event) {
        Stage stage = get_stage(event);
        try {
            stage.setScene(new_scene("othello", event));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, User.get_username(), color, strength);
        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#board");

        pane.getChildren().add(controller.getDisplay());

        System.out.println("username: " + User.get_username());
    }

    private String get_element_id(ActionEvent event) {
        Button clicked = (Button) event.getSource();

        return clicked.getId();
    }

    void set_field(ActionEvent event) {
        switch (get_element_id(event)) {
            case "b":
                color = Field.BLACK;
                break;
            case "w":
                color = Field.WHITE;
                break;
        }
    }

    void set_strength(ActionEvent event) {
        strength = Integer.parseInt(get_element_id(event));
    }

    void start(ActionEvent event) {
        Stage stage = get_stage(event);

        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, User.get_username(), color, strength);
        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#board");

        pane.getChildren().add(controller.getDisplay());

        System.out.println("username: " + User.get_username());
    }

    public void home_start(ActionEvent event) throws IOException {
        get_stage(event).setScene(new_scene("home", event));
    }


    public void board() {
        LocalGameController controller = new LocalGameController(GameInfo.REVERSI, User.get_username(), Field.BLACK, 5);
        Thread thread = new Thread(controller);
        thread.start();
        Scene scene = new Scene(controller.getDisplay());
    }

}
