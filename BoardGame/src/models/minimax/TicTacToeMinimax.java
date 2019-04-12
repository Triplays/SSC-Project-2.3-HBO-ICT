package models.minimax;

import models.config.ReversiIndicatorSet;
import models.config.TicTacToeIndicatorSet;
import models.game.Field;
import models.game.GameInfo;

public class TicTacToeMinimax extends Minimax<TicTacToeIndicatorSet>
{

    public TicTacToeMinimax(Field field, TicTacToeIndicatorSet indicatorSet){
        super(GameInfo.TICTACTOE, field);
        this.indicatorSet = indicatorSet;
    }

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

    @Override
    public void setIndicatorSet(TicTacToeIndicatorSet indicatorSet)
    {
        this.indicatorSet = indicatorSet;
    }
}
