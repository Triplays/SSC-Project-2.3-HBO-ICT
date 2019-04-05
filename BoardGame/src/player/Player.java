package player;

import controller.Controller;
import exceptions.IllegalGamePlayerException;
import exceptions.IllegalMoveException;
import game.Field;
import game.Game;

public class Player {
    private Field color;
    private String name;
    private Game game;
    private Controller controller;

    public Player(String name, Controller controller) {
        this.name = name;
        this.controller = controller;
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

    public void setGame(Game game) throws IllegalGamePlayerException {
        this.game = game;
        game.register(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void notifyPlayer() {
        controller.requestInput(this);
        System.out.println("It is " + name + "\'s turn. (Color " + color + ")");
    }

    public void move(int target) throws IllegalMoveException {
        game.move(this, target);
    }
}
