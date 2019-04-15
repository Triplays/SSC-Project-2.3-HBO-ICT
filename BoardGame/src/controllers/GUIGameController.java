package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.User;
import models.display.Display;
import models.game.Field;
import models.game.GameInfo;
import models.game.Opponent;
import models.gamecontroller.LocalGameController;

import java.io.IOException;

public class GUIGameController extends Controller{
    private static GameInfo game_type;
    private Field color = Field.BLACK;
    private int difficulty;
    public static LocalGameController controller;

    /**
     * @param event
     * @param game_type
     * @param opponent
     * @throws IOException
     *
     * sets scene of the stage to the game scene
     */
    void show(ActionEvent event, GameInfo game_type, Opponent opponent) throws IOException {
        this.game_type = game_type;

        Stage stage = get_stage(event);

        stage.setScene(new_scene("game", event));

        //Checks the enum opponent is not set to AI, if true it removes the difficulty option
        if(opponent != Opponent.AI){
            remove_item("main","difficulty", stage);
            System.out.println("Speler tegen speler");
        }
    }

    /**
    * @param event
    *
    * gets button id from the button event and returns it as an Integer
    */
    private int get_button_id(ActionEvent event) {
        //get id from clicked button
        Button clicked = (Button) event.getTarget();

        return Integer.parseInt(clicked.getId());
    }

    /**
     * @param event
     *
     * sets the color the main player uses in the match
     */
    public void set_color(ActionEvent event) {
        Stage stage = get_stage(event);
        remove_item("main" +
                "", "colorPick", stage);
        switch (get_button_id(event)) {
            case 1:
                color = Field.WHITE;
                break;
            case 2:
                color = Field.BLACK;
                break;
        }
    }

    /**
     * @param event
     *
     * sets the instance variable difficulty (which is later used to set the algorithm it's depth) and it removes the difficulty fxml item from the stage
     */
    public void set_difficulty(ActionEvent event) {
        Stage stage = get_stage(event);

        difficulty = get_button_id(event);

        remove_item("main", "difficulty", stage);
    }

    /**
     * @param stage
     *
     * gets the display of a new gamecontroller and adds it to the anchor pane of the stage
     */
    private void add_board(Stage stage) {
        AnchorPane pane = (AnchorPane) stage.getScene().lookup("#boardContainer");

        if (difficulty != 0) {
            controller = new LocalGameController(game_type, User.get_username(), color, difficulty);
        } else {
            controller = new LocalGameController(GameInfo.REVERSI, User.get_username(), Field.BLACK, "Speler 2");
        }


        Thread thread = new Thread(controller);
        thread.start();

        System.out.println(controller);

        Pane board = controller.getDisplay();
        board.setId("board");

        pane.getChildren().add(board);
        board.toFront();
    }

    /**
     * @param event
     *
     * removes start fxml item and adds board to the stage, which then places it in an anchor pane
     */
    public void start(ActionEvent event) {
        Stage stage = get_stage(event);

        remove_item("main", "start", stage);

        add_board(stage);

        System.out.println("Username: " + User.get_username());
        System.out.println("Opponent: " + controller.getOpponentname());
    }

    public void opgeven(ActionEvent event) throws IOException {
        get_stage(event).setScene(new_scene("opponent", event));
    }

    /**
     * @param event
     *
     * removes old board and adds a new board (reset the board without resetting the settings)
     */
    public void reset(ActionEvent event) {
        Stage stage = get_stage(event);
        System.out.println(stage);

        remove_item("boardContainer", "board", stage);

        add_board(stage);
    }
}