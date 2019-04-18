package models.ruleset;

import models.game.Field;
import models.game.GameState;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Ruleset implementation for Tic-tac-toe.
 */
public class TicTacToeRuleset extends Ruleset {

    /**
     * Checks whether the target location is a valid move for Tic-tac-toe. It is when the field is empty.
     * @param board the board to evaluate.
     * @param field the current player color.
     * @param target the target location.
     * @returna set of integers that would be captured by this move. Returns an empty set if there are none.
     */
    @Override
    public HashSet<Integer> legalMove(Field[] board, Field field, int target) {
        HashSet<Integer> hash = new HashSet<>();
        if (board[target] == Field.EMPTY) hash.add(target);
        return hash;
    }

    /**
     * Checks whether players need the swap, stay, or if an end condition has been met.
     * For Tic-tac-toe, the players will always swap unless one player has three fields in a row, or when
     * the board is full.
     * @param board the board to evaluate.
     * @param opponent the color of the assumed new player.
     * @return the state as determined by this method.
     */
    @Override
    public GameState checkGameState(Field[] board, Field opponent) {
        for(int i = 0; i < 3; i++) {
            if (board[i] != Field.EMPTY && board[i] == board[i+3] && board[i+3] == board[i+6])
                return fieldToGameState(board[i]);
        }
        for(int i = 0; i < 8; i+=3) {
            if (board[i] != Field.EMPTY && board[i] == board[i+1] && board[i+1] == board[i+2])
                return fieldToGameState(board[i]);
        }
        if (board[0] != Field.EMPTY && board[0] == board[4] && board[4] == board[8])
            return  fieldToGameState(board[0]);
        if (board[2] != Field.EMPTY && board[2] == board[4] && board[4] == board[6])
            return  fieldToGameState(board[2]);

        if (!Arrays.asList(board).contains(Field.EMPTY)) {
            return GameState.DRAW;
        }
        return GameState.SWAP;
    }
}
