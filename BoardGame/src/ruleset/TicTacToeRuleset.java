package ruleset;

public class TicTacToeRuleset extends Ruleset {

    @Override
    public boolean legalMove(int[][] field, int x, int y) {
        return field[x][y] == 0;
    }

    @Override
    public int gameEnded(int[][] field) {
        for(int x = 0; x < 3; x++) {
            if (field[x][0] != 0 && field[x][0] == field[x][1] && field[x][1] == field[x][2]) return field[x][0];
        }
        for(int y = 0; y < 3; y++) {
            if (field[0][y] != 0 && field[0][y] == field[1][y] && field[1][y] == field[2][y]) return field[0][y];
        }
        if (field[0][0] != 0 && field[0][0] == field[1][1] && field[1][1] == field[2][2]) return field[0][0];
        if (field[0][2] != 0 && field[0][2] == field[1][1] && field[1][1] == field[2][0]) return field[0][2];
        return 0;
    }
}
