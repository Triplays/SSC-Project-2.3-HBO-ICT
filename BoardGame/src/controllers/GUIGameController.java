package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.User;
import models.game.Field;
import models.game.GameInfo;
import models.game.Opponent;
import models.gamecontroller.LocalGameController;

import java.io.IOException;

public class GUIGameController extends Controller{
    private GameInfo last_game_type;
    private Opponent last_opponent;
    private Field color;

    void show(ActionEvent event, GameInfo game_type,Opponent opponent) throws IOException {
        last_game_type = game_type;
        last_opponent = opponent;

        Stage stage = get_stage(event);

        stage.setScene(new_scene("game", event));

        if(opponent != Opponent.AI){
            stage.getScene().lookup("#difficulty").getScene().getWindow().hide();
            new LocalGameController(game_type, User.get_username(), Field.BLACK, "Speler 2");
        }
    }

    private void remove_item(String id, Stage stage) {
        //get main pane to remove stackpane
        AnchorPane main = (AnchorPane) stage.getScene().lookup("#main");

        //remove stackpage difficulty
        main.getChildren().remove(main.lookup("#" + id));
    }

    private int get_button_id(ActionEvent event) {
        //get id from clicked button
        Button clicked = (Button) event.getTarget();

        //set difficulty
        return Integer.parseInt(clicked.getId());
    }

    public void set_color(ActionEvent event) {
        Stage stage = get_stage(event);
        remove_item("colorPick", stage);
        switch (get_button_id(event)) {
            case 1:
                color = Field.WHITE;
                break;
            case 2:
                color = Field.BLACK;
                break;
        }
    }

    public void set_difficulty(ActionEvent event) {
        Stage stage = get_stage(event);

        remove_item("difficulty", stage);

        LocalGameController controller = new LocalGameController(last_game_type, User.get_username(), color, get_button_id(event));

        Thread thread = new Thread(controller);
        thread.start();

        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#board");

        pane.getChildren().add(controller.getDisplay());

        System.out.println("Username: " + User.get_username());
        System.out.println("Opponent: " + controller.getOpponentname());
    }


    public void opgeven(ActionEvent event) throws IOException {
        get_stage(event).setScene(new_scene("opponent", event));
    }

    public void reset(ActionEvent event) throws IOException{
        new GUIGameController().show(event, last_game_type, last_opponent);
    }

}