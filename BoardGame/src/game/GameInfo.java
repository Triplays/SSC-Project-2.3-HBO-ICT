package game;

import display.Display;
import display.ReversiDisplay;
import display.TicTacToeDisplay;
import ruleset.ReversiRuleset;
import ruleset.Ruleset;
import ruleset.TicTacToeRuleset;

import java.util.Arrays;

public enum GameInfo {
    TICTACTOE("Tic-tac-toe", 3, new TicTacToeRuleset()) {
        @Override
        public Display getDisplay() { return new TicTacToeDisplay(); }

        @Override
        public Field[] getInitialBoard() {
            Field[] board = new Field[boardSize*boardSize];
            Arrays.fill(board, Field.EMPTY);
            return board;
        }
    },
    REVERSI("Reversi", 8, new ReversiRuleset()) {
        @Override
        public Display getDisplay() { return  new ReversiDisplay(); }

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

    GameInfo(String gameName, int boardSize, Ruleset ruleset) {
        this.gameName = gameName;
        this.boardSize = boardSize;
        this.ruleset = ruleset;
    }

    public abstract Display getDisplay();
    public abstract Field[] getInitialBoard();
}
