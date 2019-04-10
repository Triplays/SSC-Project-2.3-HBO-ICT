package models.player;

import models.controller.GameController;
import models.exception.UnknownGameException;
import models.exceptions.IllegalGamePlayerException;
import models.exceptions.IllegalMoveException;
import models.game.Field;
import models.game.Game;

public abstract class Player {
    Field color;
    String name;
    Game game;
    GameController gameController;

    public Player(String name)
    {
        this.name = name;
    }

    public void setColor(Field color)
    {
        this.color = color;
    }

    public Field getColor() {
        return color;
    }

    public void setController(GameController gameController) throws UnknownGameException
    {
        this.gameController = gameController;
    }

    public String getName() {
        return name;
    }

    public void setGame(Game game) throws IllegalGamePlayerException {
        this.game = game;
        game.register(this);
    }

    public abstract void notifyPlayer();

    public void move(int target) throws IllegalMoveException {
        game.move(this, target);
    }

    @Override
    public Player clone() throws CloneNotSupportedException {
        return (Player)super.clone();
    }
}
