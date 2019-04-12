package models.player;

import models.config.IndicatorSet;
import models.gamecontroller.GameController;
import models.config.ReversiIndicatorSet;
import models.config.TicTacToeIndicatorSet;
import models.exception.UnknownGameException;
import models.exceptions.IllegalMoveException;
import models.game.GameInfo;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.minimax.TicTacToeMinimax;

public class ComputerPlayer extends Player {

    private Minimax minimax;

    private int depth;

    private IndicatorSet indicatorSet;

    public ComputerPlayer(String name, int depth) {
        super(name);
        this.depth = depth;
    }

    public ComputerPlayer(String name, IndicatorSet indicatorSet) {
        super(name);
        this.indicatorSet = indicatorSet;
    }

    @Override
    public void setController(GameController gameController) throws UnknownGameException
    {
        super.setController(gameController);

        if (gameController.getGame().getGameInfo() == GameInfo.REVERSI) {

            minimax = new ReversiMinimax(this.getColor());

            if (! (this.indicatorSet instanceof ReversiIndicatorSet)) {
                if (this.indicatorSet != null) {
                    this.depth = this.indicatorSet.getDepth();
                }

                this.indicatorSet = new ReversiIndicatorSet(this.depth);
            }

            minimax.setIndicatorSet(this.indicatorSet);

        } else if(gameController.getGame().getGameInfo() == GameInfo.TICTACTOE) {

            minimax = new TicTacToeMinimax(this.getColor());

            if (! (this.indicatorSet instanceof TicTacToeIndicatorSet)) {
                if (this.indicatorSet != null) {
                    this.depth = this.indicatorSet.getDepth();
                }

                this.indicatorSet = new TicTacToeIndicatorSet(this.depth);
            }

            minimax.setIndicatorSet(this.indicatorSet);

        } else {
            throw new UnknownGameException();
        }
    }

    @Override
    public void notifyPlayer() {
        try { move(minimax.minimax(game.getBoard(), this.indicatorSet.getDepth())); }
        catch (IllegalMoveException exc) { exc.printStackTrace(); }
    }
}
