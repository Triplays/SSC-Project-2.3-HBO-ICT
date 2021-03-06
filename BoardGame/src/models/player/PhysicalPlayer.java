package models.player;

import models.gamecontroller.GameController;
import models.game.Field;

/**
 * A player representing a physical player. Has to request the controller to obtain input from the UI.
 */
public class PhysicalPlayer extends Player {
    /**
     * Constructor for a physical player.
     * @param name the name of the player.
     * @param color the color of the player.
     * @param gameController the game controller that manages this player.
     */
    public PhysicalPlayer(String name, Field color, GameController gameController) {
        super(name, color, gameController);
    }

    /**
     * Request the game controller to obtain the input from the UI.
     */
    @Override
    public void notifyPlayer() {
        gameController.requestInput(this);
    }
}
