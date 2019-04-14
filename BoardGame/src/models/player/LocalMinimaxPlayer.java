package models.player;

import models.config.ReversiIndicatorSet;
import models.config.TicTacToeIndicatorSet;
import models.exception.UnknownGameException;
import models.gamecontroller.GameController;
import models.exceptions.IllegalMoveException;
import models.game.Field;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.minimax.TicTacToeMinimax;

public class LocalMinimaxPlayer extends Player {

    private Minimax minimax;
    private int depth;

    public LocalMinimaxPlayer(String name, Field color, GameController gameController, int depth) {
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
                ReversiIndicatorSet reversiIndicatorSet = new ReversiIndicatorSet(depth);
                reversiIndicatorSet.setLineIndicator(2.7);
                minimax = new ReversiMinimax(color, reversiIndicatorSet);
                break;
            case TICTACTOE:
                TicTacToeIndicatorSet tttIndicatorSet = new TicTacToeIndicatorSet(depth);
                minimax = new TicTacToeMinimax(color, tttIndicatorSet);
                break;
        }
    }

    @Override
    public void notifyPlayer() {
        try { move(minimax.minimax(game.getBoard(), depth)); }
        catch (IllegalMoveException exc) { exc.printStackTrace(); }
    }
}
