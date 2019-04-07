package controller;

import algorithm.Minimax;
import algorithm.ReversiMinimax;
import algorithm.TicTacToeMinimax;
import game.Field;
import game.Game;
import game.GameInfo;
import player.Player;

public class LocalVersusGameContoller implements Runnable, GameController {

    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Game game;
    private boolean active = true;
    private boolean pending = false;
    private final Object o = new Object();

    private Minimax minimaxBlack = new ReversiMinimax(Field.BLACK);
    private Minimax minimaxWhite = new ReversiMinimax(Field.WHITE);

    public LocalVersusGameContoller() {
        this.game = new Game(GameInfo.REVERSI);
        this.player1 = new Player("Henk de Vries", Field.BLACK, this);
        this.player2 = new Player("Kees van Bommel", Field.WHITE, this);
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
        try {

            player1.setGame(game);
            player2.setGame(game);

            game.start();
            while (active) {
                if (pending) {
                    pending = false;
                    //synchronized (o) { o.wait(100); }
                    if (activePlayer.getColor() == Field.BLACK)
                        activePlayer.move(minimaxBlack.minimax(game.getBoard(), 7));
                    else {
                        activePlayer.move(minimaxWhite.minimax(game.getBoard(), 7));
                    }
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
}
