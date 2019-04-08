package Controllers;

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
    Scene new_scene(String view) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/Views/" + view + ".fxml"));

        return new Scene(root, 826, 551);
    }
}
