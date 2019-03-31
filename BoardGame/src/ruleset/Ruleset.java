package ruleset;

public abstract class Ruleset {

    public abstract boolean legalMove(int[][] field, int x, int y);

    public abstract int gameEnded(int[][] field);

    public int[][] allLegalMoves(int[][] field, int size) {
        int[][] possibilities = new int[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                possibilities[i][j] = legalMove(field, i, j) ? 1 : 0;
            }
        }
        return possibilities;
    }
}
