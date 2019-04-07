package controller;

import algorithm.Algorithm;
import config.IndicatorSet;
import game.Field;
import game.Game;
import game.ReversiGame;
import player.Player;

import java.util.concurrent.TimeUnit;

public class LocalReversiContoller implements Runnable, Controller
{

    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Game game;
    private boolean active = true;
    private boolean pending = false;
    private final Object o = new Object();

    public LocalReversiContoller(Player player1, Player player2)
    {
        this.game = new ReversiGame();

        this.player1 = player1;
        this.player2 = player2;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
        try {

            player1.setController(this);
            player2.setController(this);

            game.start();

            while (active) {
                TimeUnit.SECONDS.sleep(1);

                if (pending) {
                    pending = false;

                    synchronized (o) { o.wait(100); }

                    //activePlayer.move(game.giveMove(activePlayer.getColor()));
                    activePlayer.move();
                } else {
                    synchronized (o) { o.wait(); }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInput(Player player) {
        activePlayer = player;
        pending = true;
        synchronized (o) { o.notify(); }
    }

    @Override
    public void confirmation(boolean result) {

    }

    @Override
    public void matchStart(String opponentName, boolean myTurn) {

    }
}
