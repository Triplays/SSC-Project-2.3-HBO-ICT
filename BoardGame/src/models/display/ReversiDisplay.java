package models.display;

import javafx.scene.input.MouseEvent;
import models.gamecontroller.GameController;
import models.game.Field;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;



public class ReversiDisplay extends Display {

    private final int boardSize = 8;

    public ReversiDisplay(GameController gameController) {
        super(gameController);
        initiateComponents(300);
    }

    private void initiateComponents(int prefsize) {
        this.setPrefSize(prefsize, prefsize);
        boardPane.setPrefSize(prefsize, prefsize);
        piecesPane.setPrefSize(prefsize, prefsize);
        eventPane.setPrefSize(prefsize, prefsize);

        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                Rectangle box = new Rectangle();
                box.widthProperty().bind(boardPane.widthProperty().divide(boardSize));
                box.heightProperty().bind(boardPane.heightProperty().divide(boardSize));
                box.xProperty().bind(boardPane.widthProperty().divide(boardSize).multiply(i));
                box.yProperty().bind(boardPane.heightProperty().divide(boardSize).multiply(j));
                if((i % 2 == 0 && j % 2 == 1) || ( i % 2 == 1 && j % 2 == 0))
                    box.setFill(Color.LIGHTGREEN);
                else
                    box.setFill(Color.DARKGREEN);
                boardPane.getChildren().add(box);
            }
        }

        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                Rectangle box = new Rectangle();
                box.setId("Field" + (j * boardSize + i));
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
    @Override
    public void update(Field[] board) {
        Platform.runLater(() -> {
            piecesPane.getChildren().clear();
            for (int i = 0; i < boardSize*boardSize; i++){
                if (board[i] != Field.EMPTY) {
                    Circle circle = new Circle();
                    circle.radiusProperty().bind(boardPane.widthProperty().divide(boardSize * 3));
                    circle.centerXProperty().bind(boardPane.widthProperty().divide(boardSize).multiply(i%boardSize + 0.5));
                    circle.centerYProperty().bind(boardPane.heightProperty().divide(boardSize).multiply( Math.floorDiv(i, boardSize)+ 0.5));
                    if (board[i] == Field.WHITE) {
                        circle.setFill(Color.WHITE);
                    } else if (board[i] == Field.BLACK) {
                        circle.setFill(Color.BLACK);
                    }
                    piecesPane.getChildren().add(circle);
                }
            }
            eventPane.toFront();
        });
    }
}
