package models.ruleset;

import models.game.Field;
import models.game.GameState;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Rulesets define the possibility of a move in a game, and check for win conditions.
 * The Ruleset does not manage the game in any way. It can be passed a context of the game to check for validity,
 * or can be used by an algorithm or GUI to obtain the possible moves.
 */
public abstract class Ruleset {

    /**
     * Checks whether the given move is a legal one in the current context. Implemented by subclasses.
     * @param board the board to evaluate.
     * @param field the current player color.
     * @param target the target location.
     * @return a set of integers that would be captured by this move. Returns an empty set if there are none.
     */
    public abstract HashSet<Integer> legalMove(Field[] board, Field field, int target);

    /**
     * Checks how the game state should change in the current context. Implemented by subclasses.
     * @param board the board to evaluate.
     * @param opponent the color of the assumed new player.
     * @return the state as determined by this method. Actions should be taken by the caller.
     */
    public abstract GameState checkGameState(Field[] board, Field opponent);

    /**
     * Looks up all the legal moves on the board for a given player.
     * @param board the board to evaluate.
     * @param field the player to reflect the possible moves for.
     * @return a map with field coordinates as keys, and sets of integers as values, representing the fields that would
     * be captured if the move (key integer) was played.
     */
    public HashMap<Integer, HashSet<Integer>> allLegalMoves(Field[] board, Field field) {
        HashMap<Integer, HashSet<Integer>> possibilities = new HashMap<>();
        for(int i = 0; i < board.length; i++) {
            HashSet<Integer> set = legalMove(board, field, i);
            if (set.size() > 0) possibilities.put(i, legalMove(board, field, i));
        }
        return possibilities;
    }

    /**
     * Converts a Field to a GameState in case of a win determined by the method checkGameState.
     * @param field the Field to convert.
     * @return the corresponding winning GameState.
     */
    GameState fieldToGameState(Field field) {
        switch (field) {
            case WHITE:
                return GameState.WINWHITE;
            case BLACK:
                return GameState.WINBLACK;
            default:
                return null;
        }
    }
}
