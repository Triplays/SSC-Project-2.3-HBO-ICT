package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

class Controller {
    /**
     * @param mainId
     * @param id
     * @param stage
     *
     * removes fxml item of a stage using its given id, used to set game settings
     */
    void remove_item(String mainId, String id, Stage stage) {
        //get main pane to remove stackpane
        AnchorPane main = (AnchorPane) stage.getScene().lookup("#" + mainId);

        //remove stackpage difficulty
        main.getChildren().remove(main.lookup("#" + id));
    }

    /**
     * @param event
     *
     * Getting te primary stage of the game, we need this to go to a new scene
     */
    Stage get_stage(ActionEvent event) {
        return (Stage) ((Node)event.getSource()).getScene().getWindow();
    }


    /**
     * @param view
     * @return a new Scene
     * @throws IOException
     *
     * Method for creating a new scene using view to select the right fxml to load
     */
    Scene new_scene(String view, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Views/" + view + ".fxml"));
        Scene scene = ((Node)event.getSource()).getScene();

        return new Scene(root, scene.getWidth(), scene.getHeight());
    }

    /**
     *
     * @param event
     * @param view used to select an fxml file
     * @throws IOException
     *
     * sets the scene of the stage of a triggered event
     */
    void show(ActionEvent event, String view) throws IOException {
        get_stage(event).setScene(new_scene(view, event));
    }
}
