package display;

import game.Field;
import javafx.scene.layout.Pane;

public abstract class Display {

    Pane wrapperPane;
    Pane boardPane;
    Pane piecesPane;

    public Display() {
        wrapperPane = new Pane();
        boardPane = new Pane();
        piecesPane = new Pane();
    }

    public abstract void update(Field[] board);

    public Pane getWrapperPane() {
        return wrapperPane;
    }

}
