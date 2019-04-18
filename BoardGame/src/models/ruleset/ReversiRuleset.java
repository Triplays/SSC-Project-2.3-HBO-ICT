package models.ruleset;

import models.game.Field;
import models.game.GameState;

import java.util.HashSet;

/**
 * Ruleset implementation for Reversi.
 */
public class ReversiRuleset extends Ruleset {

    private HashSet<Integer> capture;

    /**
     * Checks whether this target field is a valid move on the given board for the given player.
     * For Reversi, a move is valid for the current field if:
     * - the field was empty in to begin with, and;
     * - the field in at least one of the eight possible stroke directions is of the opponent, and;
     * - in there directions where above condition applies, where there will be one or more fields taken by
     *   the opponent, ends with a field taken by the current player.
     * @param board the board to evaluate.
     * @param field the current player color.
     * @param target the target location.
     * @return a set of integers that would be captured by this move. Returns an empty set if there are none.
     */
    @Override
    public HashSet<Integer> legalMove(Field[] board, Field field, int target) {
        capture = new HashSet<>();
        if (board[target] != Field.EMPTY) return capture;
        for (Direction direction : Direction.values()) {
            directionalHelper(board, field, target, direction);
        }
        return capture;
    }

    /**
     * Checks a given direction from a starting position on a board.
     * Continues in this direction till the board limit is reached, an empty field is reached, or a field of
     * the current player is reached. In the last case, the fields traversed will be added to the capture set.
     * @param board the board to evaluate.
     * @param field the current player color.
     * @param target the target location.
     * @param direction the direction to traverse to.
     */
    private void directionalHelper(Field[] board, Field field, int target, Direction direction) {
        if (direction.limit(target)) return;
        int next = target + direction.dir;
        if (board[next] == Field.EMPTY || board[next] == field) return;
        HashSet<Integer> temp = new HashSet<>();
        temp.add(target);
        while (!direction.limit(next)) {
            temp.add(next);
            next += direction.dir;
            if (board[next] == Field.EMPTY) return;
            if (board[next] == field) {
                capture.addAll(temp);
                return;
            }
        }
    }

    /**
     * Checks whether players need the swap, stay, or if an end condition has been met.
     * In the case of Reversi, the players should swap back if the new player has no moves available, and
     * the game has ended when both players have no legal moves.
     * @param board the board to evaluate.
     * @param opponent the color of the assumed new player.
     * @return the state of the game.
     */
    @Override
    public GameState checkGameState(Field[] board, Field opponent) {
        if (allLegalMoves(board, opponent).size() > 0) return GameState.SWAP;

        Field field = opponent == Field.BLACK ? Field.WHITE : Field.BLACK;
        if (allLegalMoves(board, field).size() > 0)  return GameState.STAY;

        return countFields(board);
    }

    /**
     * Counts the fields on the board to calculate a score. Used to determine the ending game state.
     * @param board the board to evaluate.
     * @return the ending game state.
     */
    private GameState countFields(Field[] board) {
        int white = 0;
        int black = 0;
        for (Field field : board) {
            if(field == Field.WHITE) white++;
            if(field == Field.BLACK) black++;
        }

        if (white > black) return GameState.WINWHITE;
        else if (black > white) return GameState.WINBLACK;
        else return GameState.DRAW;
    }
}
