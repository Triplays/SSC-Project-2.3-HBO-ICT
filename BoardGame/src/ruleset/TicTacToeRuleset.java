package ruleset;

import game.Field;

public class TicTacToeRuleset extends Ruleset {

    @Override
    public boolean legalMove(Field[][] board, Field field, int x, int y) {
        return board[x][y] == Field.EMPTY;
    }

    @Override
    public Field gameEnded(Field[][] board) {
        for(int x = 0; x < 3; x++) {
            if (board[x][0] != Field.EMPTY && board[x][0] == board[x][1] && board[x][1] == board[x][2]) return board[x][0];
        }
        for(int y = 0; y < 3; y++) {
            if (board[0][y] != Field.EMPTY && board[0][y] == board[1][y] && board[1][y] == board[2][y]) return board[0][y];
        }
        if (board[0][0] != Field.EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0];
        if (board[0][2] != Field.EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2];
        return Field.EMPTY;
    }
}
