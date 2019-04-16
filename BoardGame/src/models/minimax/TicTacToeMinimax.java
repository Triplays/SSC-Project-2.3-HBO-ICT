package models.minimax;

import models.config.TicTacToeIndicatorSet;
import models.game.Field;
import models.game.GameInfo;

public class TicTacToeMinimax extends Minimax<TicTacToeIndicatorSet>
{

    /**
     * Configure this minimax to a player
     * @param field the maximizing Player
     * @param indicatorSet
     */
    public TicTacToeMinimax(Field field, TicTacToeIndicatorSet indicatorSet){
        super(GameInfo.TICTACTOE, field);
        this.indicatorSet = indicatorSet;
    }

    /**
     * Calculate the score for the current player on the current board for Tic-tac-toe
     * @param board the board to calculate the score onn
     * @param self the player type
     * @return
     */
    @Override
    int calculateScore(Field[] board, Field self) {
        switch(GameInfo.TICTACTOE.ruleset.checkGamestate(board, self)){
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
