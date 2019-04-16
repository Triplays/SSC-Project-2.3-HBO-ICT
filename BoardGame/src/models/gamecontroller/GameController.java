package models.gamecontroller;

import models.display.Display;
import models.game.Game;
import models.player.Player;

/**
 * A game controller manages a game and its players, obtains user input, and holds the display.
 */
public interface GameController {
    /**
     * A game can request input from the controller for a given player.
     * @param player the player who's input is expected
     */
    void requestInput(Player player);

    /**
     * Send input to the controller to be send to the game. Controller manages the active player.
     * @param move the move to perform.
     */
    void sendInput(int move);

    /**
     * Close off the controller, and end the Thread.
     */
    void closeController();

    Game getGame();
    Display getDisplay();
}
