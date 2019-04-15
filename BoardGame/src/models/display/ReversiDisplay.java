package models.display;

import models.gamecontroller.GameController;
import models.game.Field;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class ReversiDisplay extends Display {

    private final int boardSize = 8;

    public ReversiDisplay(GameController gameController) {
        super(gameController);
        initiateComponents(boardSize, 400, Color.LIGHTGREEN, Color.DARKGREEN);
    }

    @Override
    public void update(Field[] board, int scoreBlack, int scoreWhite, Field turn, String msg) {
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

            this.scoreBlack.setText("" + scoreBlack);
            this.scoreWhite.setText("" + scoreWhite);
            this.turnMessage.setText(turn.name + " is aan zet");
            this.altMessage.setText(msg);
            eventPane.toFront();
        });
    }
}
