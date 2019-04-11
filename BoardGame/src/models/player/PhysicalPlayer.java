package models.player;

import models.gamecontroller.GameController;
import models.game.Field;

public class PhysicalPlayer extends Player {
    public PhysicalPlayer(String name, Field color, GameController gameController) {
        super(name, color, gameController);
    }

    @Override
    public void notifyPlayer() {
        gameController.requestInput(this);
    }
}
