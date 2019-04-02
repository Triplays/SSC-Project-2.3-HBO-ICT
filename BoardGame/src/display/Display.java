package display;

import javafx.scene.layout.Pane;

public interface Display {

    void update(int[][] field);
    Pane getPane();

}
