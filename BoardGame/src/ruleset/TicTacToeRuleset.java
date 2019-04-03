package ruleset;

import game.Field;
import game.Gamestate;

import java.util.Arrays;
import java.util.HashSet;

public class TicTacToeRuleset extends Ruleset {

    @Override
    public HashSet<Integer> legalMove(Field[] board, Field field, int target) {
        HashSet<Integer> hash = new HashSet<>();
        if (board[target] == Field.EMPTY) hash.add(target);
        return hash;
    }

    @Override
    public Gamestate checkWinCondition(Field[] board, Field field) {
        for(int i = 0; i < 3; i++) {
            if (board[i] != Field.EMPTY && board[i] == board[i+3] && board[i+3] == board[i+6])
                return fieldToGamestate(board[i]);
        }
        for(int i = 0; i < 3; i+=3) {
            if (board[i] != Field.EMPTY && board[i] == board[i+1] && board[i+1] == board[i+2])
                return fieldToGamestate(board[i]);
        }
        if (board[0] != Field.EMPTY && board[0] == board[4] && board[4] == board[8])
            return  fieldToGamestate(board[0]);
        if (board[2] != Field.EMPTY && board[2] == board[4] && board[4] == board[6])
            return  fieldToGamestate(board[2]);

        if (!Arrays.asList(board).contains(Field.EMPTY)) {
            return Gamestate.DRAW;
        }
        return Gamestate.SWAP;
    }
}
