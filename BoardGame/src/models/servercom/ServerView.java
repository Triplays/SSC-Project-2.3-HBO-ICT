package models.servercom;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import models.game.GameInfo;
import models.gamecontroller.ServerGameController;

import java.util.HashMap;
import java.util.HashSet;

public class ServerView extends VBox {

    private GameInfo gameInfo;
    private Pane playerPane;
    private Pane challengePane;
    private ServerGameController controller;

    public ServerView(GameInfo gameInfo, ServerGameController controller) {
        this.gameInfo = gameInfo;
        this.controller = controller;
        initiateComponents();
    }

    private void initiateComponents() {
        playerPane = new VBox();
        challengePane = new VBox();

        this.setPrefSize(250, 600);
        playerPane.setPrefSize(250, 400);
        challengePane.setPrefSize(250, 200);
        this.setStyle("-fx-background-color: #FF0000");

        this.getChildren().add(playerPane);
        this.getChildren().add(challengePane);
    }

    public void update(HashSet<String> players, HashMap<Integer, String> challenges) {
        Platform.runLater(() -> {
            playerPane.getChildren().clear();
            players.forEach((player) -> {
                HBox box = new HBox();
                HBox textWrapper = new HBox();
                textWrapper.setPrefSize(150, 30);
                Text text = new Text(player);
                textWrapper.getChildren().add(text);
                box.getChildren().add(textWrapper);
                Button but = new Button("Challenge");
                but.setId("Challenge-" + player);
                but.setPrefSize(100, 30);
                but.addEventHandler(MouseEvent.MOUSE_CLICKED, onClickHandlerChallenge);
                box.getChildren().add(but);
                playerPane.getChildren().add(box);
            });

            challengePane.getChildren().clear();
            challenges.forEach((id, name) -> {
                HBox box = new HBox();
                HBox textWrapper = new HBox();
                textWrapper.setPrefSize(150, 30);
                Text text = new Text(name);
                textWrapper.getChildren().add(text);
                box.getChildren().add(textWrapper);
                Button but = new Button("Accept");
                but.setId("Accept" + id);
                but.setPrefSize(100, 30);
                but.addEventHandler(MouseEvent.MOUSE_CLICKED, onClickHandlerAccept);
                box.getChildren().add(but);
                challengePane.getChildren().add(box);
            });
        });
    }


    private EventHandler<MouseEvent> onClickHandlerAccept = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            String id = ((Node)event.getSource()).getId();
            controller.acceptChallenge(Integer.parseInt(id.substring(6)));
        }
    };

    private EventHandler<MouseEvent> onClickHandlerChallenge = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            String id = ((Node)event.getSource()).getId();
            controller.sendChallenge(id.substring(10), gameInfo);
        }
    };
}
