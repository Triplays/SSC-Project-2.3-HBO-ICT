package models.minimax;

import models.game.Field;
import models.game.GameInfo;

public class ReversiMinimax extends Minimax {

    private final int[] scoreMatrix = {
            30,-5,  5,  5,  5,  5, -5, 30,
            -5,-5,  0,  0,  0,  0, -5, -5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            -5,-5,  0,  0,  0,  0, -5, -5,
            30,-5,  5,  5,  5,  5, -5, 30
    };

    private final int[] scoreMatrixTwo = {
            100,  -25,  25,  10,  10,   40,  -25,  100,
            -25,  -50,  -5,   0,   0,   -5,  -50,  -25,
            25,   -5,   10,   2,   2,   10,   -5,   25,
            10,    0,    2,   5,   5,    2,    0,   10,
            10,    0,    2,   5,   5,    2,    0,   10,
            25,   -5,   10,   2,   2,   10,   -5,   25,
            -25,  -50,  -5,   0,   0,   -5,  -50,  -25,
            100,  -25,  25,  10,  10,   25,  -25,  100
    };

    public ReversiMinimax(Field field){
        super(GameInfo.REVERSI, field);
    }

    @Override
    int calculateScore(Field[] board, Field self) {
        int white = 0;
        int black = 0;
        for (int i =0; i < board.length; i++) {
            if(board[i] == Field.WHITE) white += scoreMatrixTwo[i];
            if(board[i] == Field.BLACK) black += scoreMatrixTwo[i];
        }

        return self == Field.BLACK ? (black - white) : (white - black);
    }
}
