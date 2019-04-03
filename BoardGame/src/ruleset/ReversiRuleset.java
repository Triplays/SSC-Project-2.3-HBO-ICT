package ruleset;

import game.Direction;
import game.Field;

public class ReversiRuleset extends Ruleset {

    private final int size = 8;

    @Override
    public boolean legalMove(Field[][] board, Field field, int x, int y) {

        // Loop through all adjacent fields
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                // Skip if:
                // - current is field itself
                // - if x/y is on an edge while i/j is pointing towards that edge
                if (!(i==0 && j==0) && (x == 0 && i == -1) && (x + i < size && y + j < size)) {
                    // Skip if the field is empty
                    if (board[x + i][y + j] == Field.EMPTY) {
                        int n = 2;
                        while (x + i*n >= 0 && x + i*n < size && y + j*n >= 0 && y + j*n < size && n != -1) {
                            if (board[x + i*n][y + j*n] == field)  n= -1;
                            if (board[x + i][y + j] == Field.EMPTY) return true;
                            n++;
                        }
                    }
                }


            }
        }

        for (Direction dir : Direction.values()) {

        }

        return false;
    }

    public boolean directionalHelper(Field[][] board, Field field, int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) return false;
        return false;
    }

    @Override
    public Field gameEnded(Field[][] board) {
        return Field.EMPTY;
    }
}
