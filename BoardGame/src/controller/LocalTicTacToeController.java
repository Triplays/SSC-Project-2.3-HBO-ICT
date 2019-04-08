package controller;

import game.Field;
import game.Game;
import game.TicTacToeGame;
import player.Player;
import player.UiPlayer;

public class LocalTicTacToeController  implements Runnable, Controller {

    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Game game;
    private boolean active = true;
    private boolean pending = false;
    private final Object o = new Object();

    public LocalTicTacToeController() {
        this.game = new TicTacToeGame();
        this.player1 = new UiPlayer("Henk de Vries");
        this.player1.setColor(Field.BLACK);
        this.player2 = new UiPlayer("Kees van Bommel");
        this.player2.setColor(Field.WHITE);
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
                if (pending) {
                    pending = false;
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
