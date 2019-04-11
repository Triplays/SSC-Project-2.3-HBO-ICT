package models.display;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import models.gamecontroller.GameController;
import models.game.Field;
import javafx.scene.layout.Pane;

public abstract class Display extends Pane {

    Pane boardPane;
    Pane piecesPane;
    Pane eventPane;
    GameController gameController;

    EventHandler<MouseEvent> onClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            String id = ((Node)event.getSource()).getId();
            System.out.println(id);
            if (id.startsWith("Field"))
                gameController.sendInput(Integer.parseInt(id.substring(5)));
        }
    };

    Display(GameController gameController) {
        boardPane = new Pane();
        piecesPane = new Pane();
        eventPane = new Pane();
        this.gameController = gameController;
    }

    public abstract void update(Field[] board);

}
