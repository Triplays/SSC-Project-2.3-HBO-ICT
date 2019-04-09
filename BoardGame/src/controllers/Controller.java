package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

class Controller {
    /**
     * @param event
     * @return
     *
     * Getting te primary stage of the game, we need this to go to a new scene
     */
    Stage get_stage(ActionEvent event) {
        return (Stage) ((Node)event.getSource()).getScene().getWindow();
    }


    /**
     * @param view
     * @return
     * @throws IOException
     *
     * Method for going to a new scene
     */
    Scene new_scene(String view, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Views/" + view + ".fxml"));
        Scene scene = ((Node)event.getSource()).getScene();

        return new Scene(root, scene.getWidth(), scene.getHeight());
    }

    /**
     *
     * @param event
     * @param view
     * @throws IOException
     *
     * Show new view
     */
    void show(ActionEvent event, String view) throws IOException {
        get_stage(event).setScene(new_scene(view, event));
    }
}
