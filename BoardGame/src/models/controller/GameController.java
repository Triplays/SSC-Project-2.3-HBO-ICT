package models.controller;

import models.game.Game;
import models.player.Player;

public interface GameController {

    void requestInput(Player player);

    Game getGame();
}
