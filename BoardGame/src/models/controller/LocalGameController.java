package models.controller;

import models.exception.UnknownGameException;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.ComputerPlayer;
import models.player.Player;

public class LocalGameController implements Runnable, GameController {

    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Game game;
    public static boolean active = true;
    private boolean pending = false;
    private final Object o = new Object();

    public LocalGameController(GameInfo gameInfo, Player player1, Player player2)
    {
        this.game = new Game(gameInfo);

        this.player1 = player1;
        this.player2 = player2;

        this.init();
    }

    public LocalGameController(GameInfo gameInfo, Player player1)
    {
        this.game = new Game(gameInfo);

        this.player1 = player1;
        this.player2 = new ComputerPlayer("Computer", 8);

        this.init();
    }

    public LocalGameController(GameInfo gameInfo)
    {
        this.game = new Game(gameInfo);

        this.player1 = new ComputerPlayer("Computer 1",8);
        this.player2 = new ComputerPlayer("Computer 2",8);

        this.init();
    }

    public void init()
    {
        try {
            this.player1.setColor(Field.BLACK);
            this.player1.setController(this);

            this.player2.setColor(Field.WHITE);
            this.player2.setController(this);
        } catch (UnknownGameException e) {
            e.printStackTrace();
        }
    }

    public Game getGame()
    {
        return game;
    }

    @Override
    public void run() {
        try {
            player1.setGame(game);
            player2.setGame(game);

            game.start();
            while (active) {}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInput(Player player) {
        activePlayer = player;
        pending = true;
        synchronized (o) { o.notify(); }
    }
}
