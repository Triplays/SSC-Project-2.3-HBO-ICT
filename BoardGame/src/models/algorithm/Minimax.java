package models.algorithm;

import models.game.Field;
import models.game.GameInfo;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Minimax {

    private GameInfo gameInfo;
    private Field self;
    private Field opponent;

    private final long timelimit = 6000;
    private long timestamp;
    private int count;

    public Minimax(GameInfo gameInfo, Field self) {
        this.gameInfo = gameInfo;
        this.self = self;
        this.opponent = self == Field.BLACK ? Field.WHITE : Field.BLACK;
    }

    public int minimax(Field[] board, int max) {
        timestamp = System.currentTimeMillis();
        count = 0;
        int result = minimax(board, 0, max, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        System.out.println("Depth " + max + " took " + (System.currentTimeMillis() - timestamp) + "ms to analyse " + count + " possible moves.");
        return result;
    }

    public int minimax(Field[] board, int depth, int max, int alpha, int beta, boolean maximizing) {

        // Stop the recursion if time runs out
        if ((System.currentTimeMillis() - timestamp) > timelimit) return calculateScore(board, self);

        // Calculate the score when max depth has been reached
        if (depth > max) return calculateScore(board, self);

        // Get all possible moves with the corresponding captured fields
        HashMap<Integer, HashSet<Integer>> moves = gameInfo.ruleset.allLegalMovesNew(board, maximizing ? self : opponent);

        //
        if (moves.size() == 0) {
            switch(gameInfo.ruleset.checkWinCondition(board, maximizing ? self : opponent)){
                case WINWHITE:
                    return self == Field.WHITE ? 1024 : -1024;
                case WINBLACK:
                    return self == Field.BLACK ? 1024 : -1024;
                case DRAW:
                    return 0;
                case STAY:
                    // TODO: continue recursion?
                    return 5;
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

    abstract int calculateScore(Field[] board, Field self);
}
