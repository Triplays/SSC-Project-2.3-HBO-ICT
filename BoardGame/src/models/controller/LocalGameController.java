package models.controller;

import javafx.scene.layout.Pane;
import models.config.IndicatorSet;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.ComputerPlayer;
import models.player.MinimaxPlayer;
import models.player.PhysicalPlayer;
import models.player.Player;

public class LocalGameController implements Runnable, GameController {

    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Game game;
    private boolean active = true;
    private boolean pending = false;
    private final Object o = new Object();

    public LocalGameController(GameInfo gameInfo, int computerOneStrenght, int computerTwoStrength) {
        this.game = new Game(gameInfo);
        this.player1 = new MinimaxPlayer("Computer niveau " + computerOneStrenght, Field.BLACK, this, computerOneStrenght);
        this.player2 = new MinimaxPlayer("Computer niveau " + computerTwoStrength, Field.WHITE, this, computerTwoStrength);
    }

    public LocalGameController(GameInfo gameInfo, String playerName, Field color, int computerStrenght) {
        this.game = new Game(gameInfo);
        this.player1 = new PhysicalPlayer(playerName, color, this);
        this.player2 = new MinimaxPlayer("Computer niveau " + computerStrenght, color == Field.WHITE ? Field.BLACK : Field.WHITE, this, computerStrenght);
    }

    public LocalGameController(GameInfo gameInfo, String playerName, Field color, String opponentName) {
        this.game = new Game(gameInfo);
        this.player1 = new PhysicalPlayer(playerName, color, this);
        this.player2 = new PhysicalPlayer(opponentName, color == Field.WHITE ? Field.BLACK : Field.WHITE, this);
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
                    if (activePlayer.getColor() == Field.BLACK) {
                        //activePlayer.move();
                    } else {
                        //activePlayer.move();
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

    @Override
    public Pane getPane() {
        return null;
    }
}
