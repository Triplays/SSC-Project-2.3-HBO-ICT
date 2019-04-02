package display;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TicTacToeDisplay implements Display {

    Pane pane;
    Pane board;
    Pane pieces;
    final int boardSize = 3;

    public TicTacToeDisplay() {
        initiateComponents();
    }

    private void initiateComponents() {
        pane = new Pane();
        board = new Pane();
        pieces = new Pane();

        board.setPrefSize(300, 300);
        pane.setPrefSize(300, 300);

        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                Rectangle box = new Rectangle();
                box.widthProperty().bind(board.widthProperty().divide(boardSize));
                box.heightProperty().bind(board.heightProperty().divide(boardSize));
                box.xProperty().bind(board.widthProperty().divide(boardSize).multiply(i));
                box.yProperty().bind(board.heightProperty().divide(boardSize).multiply(j));
                if((i%2==0 && j%2==1) || (i%2==1 && j%2==0)){
                    box.setFill(Color.WHITE);
                }else{
                    box.setFill(Color.GREEN);
                }
                board.getChildren().add(box);
            }
        }
        pane.getChildren().add(board);
        pane.getChildren().add(pieces);
    }

    @Override
    public void update(int[][] field) {
        Platform.runLater(() -> {

            pieces.getChildren().clear();
            for (int i = 0; i < boardSize; i++){
                for (int j = 0; j < boardSize; j++){
                    if (field[i][j] != 0) {
                        Circle circle = new Circle();
                        circle.radiusProperty().bind(board.widthProperty().divide(boardSize * 3));
                        circle.centerXProperty().bind(board.widthProperty().divide(boardSize).multiply(i + 0.5));
                        circle.centerYProperty().bind(board.heightProperty().divide(boardSize).multiply(j + 0.5));
                        if (field[i][j] == 1) {
                            circle.setFill(Color.RED);
                        } else if (field[i][j] == 2) {
                            circle.setFill(Color.ORANGE);
                        }
                        pieces.getChildren().add(circle);
                    }
                }
            }

        });
    }

    @Override
    public Pane getPane() {
        return pane;
    }
}
