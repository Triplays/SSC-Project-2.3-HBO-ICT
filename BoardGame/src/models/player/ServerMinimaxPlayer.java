package models.player;

import models.config.ReversiIndicatorSet;
import models.config.TicTacToeIndicatorSet;
import models.game.Field;
import models.gamecontroller.GameController;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.minimax.TicTacToeMinimax;

/**
 * A minimax/computer player intended to be used with the server.
 * This player does send the calculated input back to the controller, to send it to the server without updating
 * the local game board and state. This will be done by the controller on confirmation.
 * TODO: merge local en server minimax player to remove redundancy. Requires LocalGameController changes.
 */
public class ServerMinimaxPlayer extends Player {

    private Minimax minimax;
    private int depth;

    /**
     * Constructor for the minimax player playinng on a server
     * @param name name of the computer player.
     * @param color color of the computer player.
     * @param gameController the game controller that manages this player.
     * @param depth the depth, or difficulty, of the computer player.
     */
    public ServerMinimaxPlayer(String name, Field color, GameController gameController, int depth) {
        super(name, color, gameController);

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

    /**
     * Notify the controller with the result of the minimax analysis.
     */
    @Override
    public void notifyPlayer() {
        gameController.sendInput(minimax.minimax(game.getBoard(), depth));
    }
}
