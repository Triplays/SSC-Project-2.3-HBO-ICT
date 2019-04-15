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
    void remove_item(String mainId, String id, Stage stage) {
        //get main pane to remove stackpane
        AnchorPane main = (AnchorPane) stage.getScene().lookup("#" + mainId);

        //remove stackpage difficulty
        main.getChildren().remove(main.lookup("#" + id));
    }

    /**
     * @param event
     * @return
     *
     * Getting te primary stage of the game, we need this to go to a new scene
     */
    Stage get_stage(ActionEvent event) {
        Stage stage =(Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setMinHeight(590);
        stage.setMinWidth(825);
        return stage;
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
