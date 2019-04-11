package models.display;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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


    void initiateComponents(int boardSize, int prefSize, Color primaryColor, Color secondaryColor) {
        this.setPrefSize(prefSize, prefSize);
        boardPane.setPrefSize(prefSize, prefSize);
        piecesPane.setPrefSize(prefSize, prefSize);
        eventPane.setPrefSize(prefSize, prefSize);

        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                Rectangle box = new Rectangle();
                box.widthProperty().bind(boardPane.widthProperty().divide(boardSize));
                box.heightProperty().bind(boardPane.heightProperty().divide(boardSize));
                box.xProperty().bind(boardPane.widthProperty().divide(boardSize).multiply(i));
                box.yProperty().bind(boardPane.heightProperty().divide(boardSize).multiply(j));
                if((i % 2 == 0 && j % 2 == 1) || ( i % 2 == 1 && j % 2 == 0))
                    box.setFill(primaryColor);
                else
                    box.setFill(secondaryColor);
                boardPane.getChildren().add(box);
            }
        }
        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                Rectangle box = new Rectangle();
                box.setId("Field" + (j*boardSize + i));
                box.widthProperty().bind(eventPane.widthProperty().divide(boardSize));
                box.heightProperty().bind(eventPane.heightProperty().divide(boardSize));
                box.xProperty().bind(eventPane.widthProperty().divide(boardSize).multiply(i));
                box.yProperty().bind(eventPane.heightProperty().divide(boardSize).multiply(j));
                box.setFill(Color.TRANSPARENT);
                box.addEventHandler(MouseEvent.MOUSE_CLICKED, onClickHandler);
                eventPane.getChildren().add(box);
            }
        }
        this.getChildren().add(boardPane);
        this.getChildren().add(piecesPane);
        this.getChildren().add(eventPane);
        eventPane.toFront();
    }

    public abstract void update(Field[] board);

}
