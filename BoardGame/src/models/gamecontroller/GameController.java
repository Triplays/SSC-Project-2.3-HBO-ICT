package models.gamecontroller;

import models.display.Display;
import models.game.Game;
import models.player.Player;

public interface GameController {
    void requestInput(Player player);
    Game getGame();
    Display getDisplay();
    void sendInput(int move);
    void closeController();
}
