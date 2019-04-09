package models.controller;

import models.config.IndicatorSet;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.ComputerPlayer;
import models.player.MinimaxPlayer;
import models.player.Player;

public class LocalShowdownGameController  implements Runnable, GameController {

    private Player player1;
    private Player player2;
    private Game game;
    private boolean active = true;

    public LocalShowdownGameController() {
        this.game = new Game(GameInfo.REVERSI);
        this.player1 = new MinimaxPlayer("Frieso", Field.BLACK, this, 4);
        IndicatorSet indicatorSet = new IndicatorSet(6, 0.35, 4.0, 3.5);
        this.player2 = new ComputerPlayer("Bommeltje", Field.WHITE, this, indicatorSet);
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
            while (active) {}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInput(Player player) {}
}
