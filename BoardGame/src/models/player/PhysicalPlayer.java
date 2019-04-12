package models.player;

import models.exception.UnknownGameException;
import models.gamecontroller.GameController;
import models.game.Field;

public class PhysicalPlayer extends Player {
    public PhysicalPlayer(String name, Field color, GameController controller) {
        super(name);

        try {
            this.setController(gameController);
            this.setColor(color);
        } catch (UnknownGameException e) {
            System.out.println("Unable to load player.");
        }
    }

    @Override
    public void notifyPlayer() {
        gameController.requestInput(this);
    }
}
