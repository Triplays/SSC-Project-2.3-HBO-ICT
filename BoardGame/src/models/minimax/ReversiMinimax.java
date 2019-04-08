package models.minimax;

import models.game.Field;
import models.game.GameInfo;

public class ReversiMinimax extends Minimax {

    private final int[] scorematrix = {
            30,-5,  5,  5,  5,  5, -5, 30,
            -5,-5,  0,  0,  0,  0, -5, -5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            -5,-5,  0,  0,  0,  0, -5, -5,
            30,-5,  5,  5,  5,  5, -5, 30
    };

    public ReversiMinimax(Field field){
        super(GameInfo.REVERSI, field);
    }

    @Override
    int calculateScore(Field[] board, Field self) {
        int white = 0;
        int black = 0;
        for (int i =0; i < board.length; i++) {
            if(board[i] == Field.WHITE) white += scorematrix[i];
            if(board[i] == Field.BLACK) black += scorematrix[i];
        }

        return self == Field.BLACK ? (black - white) : (white - black);
    }
}
