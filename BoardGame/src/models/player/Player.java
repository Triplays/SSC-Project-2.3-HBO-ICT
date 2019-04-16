package models.player;

import models.gamecontroller.GameController;
import models.exceptions.IllegalGamePlayerException;
import models.exceptions.IllegalMoveException;
import models.game.Field;
import models.game.Game;

/**
 *
 */
public abstract class Player {
    Field color;
    String name;
    Game game;
    GameController gameController;

    public Player(String name) { this.name = name; }

    public void setColor(Field color) { this.color = color; }
    public Field getColor() { return color; }
    public String getName() { return name; }
    public void setController(GameController gameController) { this.gameController = gameController;}

    /**
     * Bind this player to a game, and register to that game.
     * @param game the game to be registered to.
     * @throws IllegalGamePlayerException if the color was already taken, or when there are already two players.
     */
    public void setGame(Game game) throws IllegalGamePlayerException {
        this.game = game;
        game.register(this);
    }

    /**
     * The game requests the player to perform a move. Implemented by the subclasses.
     */
    public abstract void notifyPlayer();

    /**
     * Make the player perform a move on the game he is playing.
     * @param target the target location the perform the move on
     * @throws IllegalMoveException when the game has not started, it was not your turn, or when the move is illegal.
     */
    public void move(int target) throws IllegalMoveException {
        game.move(this, target);
    }

    @Override
    public Player clone() throws CloneNotSupportedException {
        return (Player)super.clone();
    }
}
