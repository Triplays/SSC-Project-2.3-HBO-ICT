package algorithm;

import game.Field;
import game.GameInfo;

public class TicTacToeMinimax extends Minimax {

    public TicTacToeMinimax(Field field){ super(GameInfo.TICTACTOE, field); }

    @Override
    int calculateScore(Field[] board, Field self) {
        switch(GameInfo.TICTACTOE.ruleset.checkWinCondition(board, self)){
            case WINWHITE:
                return self == Field.WHITE ? 64 : -64;
            case WINBLACK:
                return self == Field.BLACK ? 64 : -64;
            case DRAW:
                return 0;
            case SWAP:
                return 0;
        }
        return 0;
    }
}
