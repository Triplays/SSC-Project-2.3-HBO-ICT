package models.controller;

import models.config.IndicatorSet;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.player.ComputerPlayer;
import models.player.PhysicalPlayer;
import models.player.Player;

public class LocalComputerGameController implements Runnable, GameController {

    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Game game;
    private boolean active = true;
    private boolean pending = false;
    private final Object o = new Object();

    private Minimax minimaxBlack = new ReversiMinimax(Field.BLACK);

    public LocalComputerGameController() {
        this.game = new Game(GameInfo.REVERSI);
        this.player1 = new PhysicalPlayer("Henk de Vries", Field.BLACK, this);

        // Temporary location
        IndicatorSet indicatorSet = new IndicatorSet(6, 0.35, 4.0, 3.5);
        this.player2 = new ComputerPlayer("Kees van Bommel", Field.WHITE, this, indicatorSet);
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
                    // TODO: Obtain user input
                    player1.move(minimaxBlack.minimax(game.getBoard(), 5));
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
