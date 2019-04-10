package models.controller;

import javafx.scene.layout.Pane;
import models.game.Game;
import models.player.Player;

public interface GameController {

    void requestInput(Player player);

    Game getGame();

    Pane getPane();

    //void closeController();
}
