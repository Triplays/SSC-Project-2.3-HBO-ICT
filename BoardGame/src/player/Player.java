package player;

import exceptions.IllegalMoveException;
import game.Field;
import game.Game;

public class Player {
    private Field color;
    private String name;
    private Game game;

    public Player(String name) {
        this.name = name;
    }

    public void setColor(Field color) {
        this.color = color;
    }

    public Field getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setGame(Game game) {
        this.game = game;
        game.register(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateTurn(int lastSet) {
        // TODO: Proper implementation
        System.out.println("My turn, last set is " + lastSet +", my set: ");
    }

    public void move(int x, int y) throws IllegalMoveException {
        game.move(this, x, y);
    }

    public void move(int target) throws IllegalMoveException {
        game.move(this, target);
    }
}
