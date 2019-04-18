package models.display;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.gamecontroller.GameController;
import models.game.Field;
import javafx.scene.layout.Pane;

public abstract class Display extends VBox {

    Pane wrapperPane;
    Pane scorePane;
    Pane boardPane;
    Pane piecesPane;
    Pane eventPane;
    Text scoreBlack;
    Text scoreWhite;
    Text turnMessage;
    Text altMessage;
    GameController gameController;

    EventHandler<MouseEvent> onClickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            String id = ((Node)event.getSource()).getId();
            if (id.startsWith("Field"))
                gameController.sendInput(Integer.parseInt(id.substring(5)));
        }
    };

    Display(GameController gameController) {
        wrapperPane = new Pane();
        scorePane = new HBox(10);
        boardPane = new Pane();
        piecesPane = new Pane();
        eventPane = new Pane();
        this.gameController = gameController;
        this.setSpacing(20);
    }

    /**
     * Initiate the initals panels, and set their sizes. Add panes to each other.
     * @param boardSize the size of the board to be drawn.
     * @param prefSize the preferred size of the board.
     * @param primaryColor the primary color of the files.
     * @param secondaryColor the secondary color of the tiles.
     */
    void initiateComponents(int boardSize, int prefSize, Color primaryColor, Color secondaryColor) {
        wrapperPane.setPrefSize(prefSize, prefSize);
        boardPane.setPrefSize(prefSize, prefSize);
        piecesPane.setPrefSize(prefSize, prefSize);
        eventPane.setPrefSize(prefSize, prefSize);
        scorePane.setPrefSize(prefSize, 100);

        drawBoards(boardSize, primaryColor, secondaryColor);
        drawScore();

        wrapperPane.getChildren().add(boardPane);
        wrapperPane.getChildren().add(piecesPane);
        wrapperPane.getChildren().add(eventPane);
        this.getChildren().add(wrapperPane);
        this.getChildren().add(scorePane);
        eventPane.toFront();
    }

    /**
     * Draw the score and text interface to the display.
     */
    private void drawScore() {
        Circle circle = new Circle(16);
        circle.setFill(Color.BLACK);
        scorePane.getChildren().add(circle);

        scoreBlack = new Text("0");
        scoreBlack.setFont(Font.font("Calibri", 30));
        scorePane.getChildren().add(scoreBlack);

        circle = new Circle(16);
        circle.setFill(Color.WHITE);
        scorePane.getChildren().add(circle);

        scoreWhite = new Text("0");
        scoreWhite.setFont(Font.font("Calibri", 30));
        scorePane.getChildren().add(scoreWhite);

        circle = new Circle(5);
        circle.setFill(Color.TRANSPARENT);
        scorePane.getChildren().add(circle);

        VBox box = new VBox(5);

        turnMessage = new Text("Spel is nog niet gestart");
        turnMessage.setWrappingWidth(200);
        turnMessage.setFont(Font.font("Calibri", 16));

        altMessage = new Text("");
        altMessage.setWrappingWidth(200);
        altMessage.setFont(Font.font("Calibri", 12));

        box.getChildren().add(turnMessage);
        box.getChildren().add(altMessage);
        scorePane.getChildren().add(box);
    }

    /**
     * Draw the board en add event listeners to each field.
     * @param boardSize the size of the board to be drawn.
     * @param primaryColor the primary color of the files.
     * @param secondaryColor the secondary color of the tiles.
     */
    private void drawBoards(int boardSize, Color primaryColor, Color secondaryColor) {
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
    }

    /**
     * Update the fields of the board according to the display implementation of the subclasses.
     * @param board the board to be displayed.
     * @param scoreBlack the score of black
     * @param scoreWhite the score of white
     * @param turn the color who's turn it is.
     * @param msg optional message to be displayed.
     */
    public abstract void update(Field[] board, int scoreBlack, int scoreWhite, Field turn, String msg);

}
