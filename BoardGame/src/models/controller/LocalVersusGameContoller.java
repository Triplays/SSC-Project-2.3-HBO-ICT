package models.controller;

import models.config.IndicatorSet;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.ComputerPlayer;
import models.player.PhysicalPlayer;
import models.player.Player;

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
        this.player1 = new PhysicalPlayer("Frieso", Field.BLACK, this);
        this.player2 = new PhysicalPlayer("Bommeltje", Field.WHITE, this);
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
                    if (activePlayer.getColor() == Field.BLACK)
                        activePlayer.move(minimaxBlack.minimax(game.getBoard(), 7));
                    else {
                        activePlayer.move(minimaxWhite.minimax(game.getBoard(), 6));
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
