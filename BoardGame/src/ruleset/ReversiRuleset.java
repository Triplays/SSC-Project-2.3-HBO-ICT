package ruleset;

import game.Direction;
import game.Field;
import game.Gamestate;

import java.util.Arrays;
import java.util.HashSet;

public class ReversiRuleset extends Ruleset {

    private HashSet<Integer> capture;

    @Override
    public HashSet<Integer> legalMove(Field[] board, Field field, int target) {
        capture = new HashSet<>();
        if (board[target] != Field.EMPTY) return capture;
        for (Direction direction : Direction.values()) {
            directionalHelper(board, field, target, direction);
        }
        return capture;
    }

    public void directionalHelper(Field[] board, Field field, int target, Direction direction) {
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

    @Override
    public Gamestate checkWinCondition(Field[] board, Field opponent) {
        if ((Arrays.stream(allLegalMoves(board, opponent, 8)).sum()) > 0) return Gamestate.SWAP;

        Field field = opponent == Field.BLACK ? Field.WHITE : Field.BLACK;
        if ((Arrays.stream(allLegalMoves(board, field, 8)).sum()) > 0)  return Gamestate.STAY;

        return countFields(board);
    }

    private Gamestate countFields(Field[] board) {
        int white = 0;
        int black = 0;
        for (Field field : board) {
            switch (field) {
                case WHITE:
                    white++;
                case BLACK:
                    black++;
                default:
                    break;
            }
        }
        if (white > black) return Gamestate.WINWHITE;
        else if (black > white) return Gamestate.WINBLACK;
        else return Gamestate.DRAW;
    }
}
