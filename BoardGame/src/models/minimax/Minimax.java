package models.minimax;

import models.config.IndicatorSet;
import models.game.Field;
import models.game.GameInfo;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Minimax<T extends IndicatorSet> {

    protected T indicatorSet;

    private GameInfo gameInfo;
    private Field self;
    protected Field opponent;

    private final long timelimit = 8000;
    private long timestamp;
    private int count;

    public Minimax(GameInfo gameInfo, Field self) {
        this.gameInfo = gameInfo;
        this.self = self;
        this.opponent = self == Field.BLACK ? Field.WHITE : Field.BLACK;
    }

    /**
     * Initialises a minimax analysis. Sets a timestamp as reference point.
     * @param board the inital board to analyse.
     * @param max the maximum depth to recurse.
     * @return
     */
    public int minimax(Field[] board, int max) {
        timestamp = System.currentTimeMillis();
        count = 0;
        int result = minimax(board, 0, max, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        System.out.println("Depth " + max + " took " + (System.currentTimeMillis() - timestamp) + "ms to analyse " + count + " possible moves.");
        return result;
    }

    /**
     * Perform a minimax analysis. Calls itself recursively.
     * Stops when the depth is reached, when the time limit is reached, or when the move set is depleted.
     * @param board the board to analyse in this iteration.
     * @param depth the current depth of the recursion.
     * @param max the max depth of the recursion.
     * @param alpha value for pruning maximizing decision paths.
     * @param beta value for pruning minimizing decision paths.
     * @param maximizing whether the current player is attempting to maximize or minimize the score.
     * @return
     */
    private int minimax(Field[] board, int depth, int max, int alpha, int beta, boolean maximizing) {

        // Stop the recursion if time runs out
        if ((System.currentTimeMillis() - timestamp) > timelimit) return calculateScore(board, self);

        // Calculate the score when max depth has been reached
        if (depth > max) return calculateScore(board, self);

        // Get all possible moves with the corresponding captured fields
        HashMap<Integer, HashSet<Integer>> moves = gameInfo.ruleset.allLegalMoves(board, maximizing ? self : opponent);

        // Finalize scoring matrix when the move set is empty. Could be delegated to the subclasses in the future
        if (moves.size() == 0) {
            switch(gameInfo.ruleset.checkGameState(board, maximizing ? self : opponent)){
                case WINWHITE:
                    return self == Field.WHITE ? 4096 : -4096;
                case WINBLACK:
                    return self == Field.BLACK ? 4096 : -4096;
                case DRAW:
                    return 0;
                case STAY:
                    return maximizing ? 1024 : -1024;
            }
        }

        // Toggle behaviour for minimizing and maximizing
        if (maximizing) {
            int bestMove = 0;
            for (HashMap.Entry<Integer, HashSet<Integer>> move : moves.entrySet()) {
                Field[] newBoard = board.clone();
                for (Integer loc : move.getValue()) newBoard[loc] = self;
                int result = minimax(newBoard, depth + 1, max, alpha, beta, false);
                count++;

                // Update alpha if the result is the new high, and store the move
                if (result > alpha) {
                    alpha = result;
                    bestMove = move.getKey();
                }

                // Prune if alpha is greater than beta anyway
                if (alpha >= beta) break;
            }
            // return the best move if we are back at the top (Done with recursion), else return alpha
            if (depth == 0) return bestMove;
            return alpha;
        } else {
            for (HashMap.Entry<Integer, HashSet<Integer>> move : moves.entrySet()) {
                Field[] newBoard = board.clone();
                for (Integer loc : move.getValue()) newBoard[loc] = opponent;
                int result = minimax(newBoard, depth + 1, max, alpha, beta, true);
                count++;

                // Update beta if the result is the new low
                if (result < beta) beta = result;

                // Prune if alpha is greater than beta anyway
                if (alpha >= beta) break;
            }
            return beta;
        }
    }

    /**
     * Calculate the score based on a subclass' implementation
     * @param board the board to calculate the score onn
     * @param self the player type
     * @return the score from the maximizing player's perspective
     */
    abstract int calculateScore(Field[] board, Field self);
}
