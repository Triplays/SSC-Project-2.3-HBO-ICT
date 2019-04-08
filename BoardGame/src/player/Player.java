package player;

import controller.Controller;
import exceptions.IllegalGamePlayerException;
import exceptions.IllegalMoveException;
import game.Field;
import game.Game;

public abstract class Player implements Cloneable
{

    protected Field color;
    protected String name;
    protected Game game;
    protected Controller controller;

    public Player(String name) {
        this.name = name;
    }

    public void setColor(Field color) {
        this.color = color;
    }

    public void setController(Controller controller) throws IllegalGamePlayerException
    {
        this.controller = controller;
        this.setGame(controller.getGame());
    }

    public void setGame(Game game) throws IllegalGamePlayerException
    {
        this.game = game;
        game.register(this);
    }

    public Field getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void notifyPlayer() {
        controller.requestInput(this);
        System.out.println("It is " + name + "\'s turn. (Color " + color + ")");
    }

    public void move() throws IllegalMoveException, Exception
    {
        game.move(this, this.getMove());
    }

    public abstract Integer getMove() throws Exception;

    @Override
    public Player clone() throws CloneNotSupportedException
    {
        return (Player)super.clone();
    }

}
