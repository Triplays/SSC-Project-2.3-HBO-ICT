package ruleset;

import game.Field;
import game.Gamestate;

import java.util.HashSet;

public abstract class Ruleset {

    public abstract HashSet<Integer> legalMove(Field[] board, Field field, int target);

    public abstract Gamestate checkWinCondition(Field[] board, Field field);

    public int[] allLegalMoves(Field[] board, Field field, int size) {
        int[] possibilities = new int[size*size];
        for(int i = 0; i < size*size; i++) {
                possibilities[i] = legalMove(board, field, i).size();
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
