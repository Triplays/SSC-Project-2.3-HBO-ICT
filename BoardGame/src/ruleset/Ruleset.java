package ruleset;

import game.Field;

public abstract class Ruleset {

    public abstract boolean legalMove(Field[][] board, Field field, int x, int y);

    public abstract Field gameEnded(Field[][] board);

    public Field[][] allLegalMoves(Field[][] board, Field field, int size) {
        Field[][] possibilities = new Field[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                possibilities[i][j] = legalMove(board, field, i, j) ? Field.LEGAL : Field.EMPTY;
            }
        }
        return possibilities;
    }
}
