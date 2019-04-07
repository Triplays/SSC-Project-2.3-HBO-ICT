package algorithm;

import game.Field;
import game.GameInfo;

public class ReversiMinimax extends Minimax {

    public ReversiMinimax(Field field){
        super(GameInfo.REVERSI, field);
    }

    @Override
    int calculateScore(Field[] board) {
        int white = 0;
        int black = 0;
        for (Field field : board) {
            if(field == Field.WHITE) white++;
            if(field == Field.BLACK) black++;
        }

        return self == Field.BLACK ? (black - white) : (white - black);
    }
}
