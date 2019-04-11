package models.player;

import models.helper.PositionHelper;
import models.algorithm.Algorithm;
import models.config.IndicatorSet;
import models.gamecontroller.GameController;
import models.exceptions.IllegalGamePlayerException;
import models.game.Field;

public class ComputerPlayer extends Player
{

    private Algorithm algorithm;

    private IndicatorSet indicatorSet;

    public ComputerPlayer(String name, Field color, GameController gameController, IndicatorSet indicatorSet) {
        super("Computer " + name, color, gameController);
        this.indicatorSet = indicatorSet;

        this.algorithm = new Algorithm(gameController.getGame().getBoard(), this.indicatorSet, this);
        this.algorithm.setGame(gameController.getGame());
    }

    @Override
    public void setController(GameController gameController) throws IllegalGamePlayerException {
        super.setController(gameController);
        this.algorithm = new Algorithm(gameController.getGame().getBoard(), this.indicatorSet, this);
        this.algorithm.setGame(gameController.getGame());
    }

    public Integer getMove() throws Exception {
        Long tmp = System.currentTimeMillis();
        Integer move = this.algorithm.getMove();

        System.out.println("Calculation time: " + (System.currentTimeMillis() - tmp) + " ms");
        System.out.println("Move: x-" + PositionHelper.getX(move) + ", y-" + PositionHelper.getY(move));

        return move;
    }

    @Override
    public void notifyPlayer() {
        try { move(getMove()); }
        catch (Exception exc) { exc.printStackTrace(); }
    }
}
