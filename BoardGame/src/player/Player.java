package player;

import controller.GameController;
import exceptions.IllegalGamePlayerException;
import exceptions.IllegalMoveException;
import game.Field;
import game.Game;

public class Player {
    private Field color;
    private String name;
    private Game game;
    private GameController gameController;

    public Player(String name, Field color, GameController gameController) {
        this.name = name;
        this.gameController = gameController;
        this.color = color;
    }

    public Field getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setGame(Game game) throws IllegalGamePlayerException {
        this.game = game;
        game.register(this);
    }

    public void notifyPlayer() {
        gameController.requestInput(this);
    }

    public void move(int target) throws IllegalMoveException {
        game.move(this, target);
    }
}
