package models.game;

import models.gamecontroller.GameController;
import models.display.Display;
import models.display.ReversiDisplay;
import models.display.TicTacToeDisplay;
import models.ruleset.ReversiRuleset;
import models.ruleset.Ruleset;
import models.ruleset.TicTacToeRuleset;

import java.util.Arrays;

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

    GameInfo(String gameName, int boardSize, Ruleset ruleset) {
        this.gameName = gameName;
        this.boardSize = boardSize;
        this.ruleset = ruleset;
    }

    public abstract Display getDisplay(GameController gamecontroller);
    public abstract Field[] getInitialBoard();
}
