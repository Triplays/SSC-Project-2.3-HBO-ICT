package controllers;

import models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.io.IOException;


public class StartController extends Controller {

    /**
     * @param event
     * @throws IOException
     *
     * gets the entered username from text field and set it as the username for User, then it changes the scene to Home
     */
    public void save_username(ActionEvent event) throws IOException {
        TextField username = (TextField) get_stage(event).getScene().lookup("#username");

        User.create(username.getText());

        //go to home
        new HomeController().show(event, "Home");
    }

}
