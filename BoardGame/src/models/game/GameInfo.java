package models.game;

import models.gamecontroller.GameController;
import models.display.Display;
import models.display.ReversiDisplay;
import models.display.TicTacToeDisplay;
import models.ruleset.ReversiRuleset;
import models.ruleset.Ruleset;
import models.ruleset.TicTacToeRuleset;

import java.util.Arrays;

/**
 * Collection of supported games, with their initial game settings.
 */
public enum GameInfo {
    TICTACTOE("Tic-tac-toe", 3, new TicTacToeRuleset()) {
        @Override
        public Display getDisplay(GameController gamecontroller) { return new TicTacToeDisplay(gamecontroller); }

        @Override
        public Field[] getInitialBoard() {
            Field[] board = new Field[boardSize*boardSize];
            Arrays.fill(board, Field.EMPTY);
            return board;
        }
    },
    REVERSI("Reversi", 8, new ReversiRuleset()) {
        @Override
        public Display getDisplay(GameController gamecontroller) { return  new ReversiDisplay(gamecontroller); }

        @Override
        public Field[] getInitialBoard() {
            Field[] board = new Field[boardSize*boardSize];
            Arrays.fill(board, Field.EMPTY);
            board[27] = Field.WHITE;
            board[36] = Field.WHITE;
            board[28] = Field.BLACK;
            board[35] = Field.BLACK;
            return board;
        }
    };

    public final String gameName;
    public final int boardSize;
    public final Ruleset ruleset;

    /**
     * @param gameName      name of the game.
     * @param boardSize     board size for the game. Boards are always square.
     * @param ruleset       rule set to be applied on this game.
     */
    GameInfo(String gameName, int boardSize, Ruleset ruleset) {
        this.gameName = gameName;
        this.boardSize = boardSize;
        this.ruleset = ruleset;
    }

    /**
     * Obtain an instance of the default display of the game.
     * @param gamecontroller the GameController that manages this Display
     * @return the Display
     */
    public abstract Display getDisplay(GameController gamecontroller);

    /**
     * Obtain the initial board for the game.
     * @return board with the correct size and starting fields.
     */
    public abstract Field[] getInitialBoard();
}
