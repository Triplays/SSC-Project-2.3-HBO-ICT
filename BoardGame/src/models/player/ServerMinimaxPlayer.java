package models.player;

import models.config.ReversiIndicatorSet;
import models.config.TicTacToeIndicatorSet;
import models.exception.UnknownGameException;
import models.game.Field;
import models.gamecontroller.GameController;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.minimax.TicTacToeMinimax;

public class ServerMinimaxPlayer extends Player {

    private Minimax minimax;
    private int depth;

    public ServerMinimaxPlayer(String name, Field color, GameController gameController, int depth) {
        super(name);

        try {
            this.setController(gameController);
            this.setColor(color);
        } catch (UnknownGameException e) {
            System.out.println("Unable to load player.");
        }

        this.depth = depth;
        switch (gameController.getGame().getGameInfo()) {
            case REVERSI:
                ReversiIndicatorSet reversiIndicatorSet = new ReversiIndicatorSet(8);
                reversiIndicatorSet.setLineIndicator(2.7);
                minimax = new ReversiMinimax(color, reversiIndicatorSet);
                break;
            case TICTACTOE:
                TicTacToeIndicatorSet tttIndicatorSet = new TicTacToeIndicatorSet(8);
                minimax = new TicTacToeMinimax(color, tttIndicatorSet);
                break;
        }
    }

    @Override
    public void notifyPlayer() {
        gameController.sendInput(minimax.minimax(game.getBoard(), depth));
    }
}
