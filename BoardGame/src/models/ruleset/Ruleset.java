package models.ruleset;

import models.game.Field;
import models.game.Gamestate;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Ruleset {

    public abstract HashSet<Integer> legalMove(Field[] board, Field field, int target);

    public abstract Gamestate checkWinCondition(Field[] board, Field field);

    public int[] allLegalMoves(Field[] board, Field field) {
        int[] possibilities = new int[board.length];
        for(int i = 0; i < board.length; i++) {
                possibilities[i] = legalMove(board, field, i).size();
        }
        return possibilities;
    }

    public HashMap<Integer, HashSet<Integer>> allLegalMovesNew(Field[] board, Field field) {
        HashMap<Integer, HashSet<Integer>> possibilities = new HashMap<>();
        for(int i = 0; i < board.length; i++) {
            HashSet<Integer> set = legalMove(board, field, i);
            if (set.size() > 0) possibilities.put(i, legalMove(board, field, i));
        }
        return possibilities;
    }

    Gamestate fieldToGamestate(Field field) {
        switch (field) {
            case WHITE:
                return Gamestate.WINWHITE;
            case BLACK:
                return Gamestate.WINBLACK;
            default:
                return null;
        }
    }
}
