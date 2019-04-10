package models.player;

import models.controller.GameController;
import models.game.Field;

public class PhysicalPlayer extends Player {
    public PhysicalPlayer(String name) {
        super(name);
    }

    @Override
    public void notifyPlayer() {
        gameController.requestInput(this);
    }
}
