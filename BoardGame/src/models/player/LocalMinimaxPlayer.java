package models.player;

import models.config.ReversiIndicatorSet;
import models.config.TicTacToeIndicatorSet;
import models.gamecontroller.GameController;
import models.exceptions.IllegalMoveException;
import models.game.Field;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.minimax.TicTacToeMinimax;

/**
 * The local computer/minimax player bypasses the controller when asked for a move.
 * Instead, it executes the minimax algorithm, and plays it directly to the game board.
 */
public class LocalMinimaxPlayer extends Player {

    private Minimax minimax;
    private int depth;

    /**
     * Constructor for a local computer/minimax player.
     * @param name name of the computer player.
     * @param color color of the computer player.
     * @param gameController the game controller that manages this player.
     * @param depth the depth, or difficulty, of the computer player.
     */
    public LocalMinimaxPlayer(String name, Field color, GameController gameController, int depth) {
        super(name, color, gameController);
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

    /**
     * Execute the minimax algorithm, and play it directly to the game board.
     */
    @Override
    public void notifyPlayer() {
        try { move(minimax.minimax(game.getBoard(), depth)); }
        catch (IllegalMoveException exc) { exc.printStackTrace(); }
    }
}
