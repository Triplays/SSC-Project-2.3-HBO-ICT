package player;

import helper.PositionHelper;
import algorithm.Algorithm;
import config.IndicatorSet;
import controller.Controller;
import exceptions.IllegalGamePlayerException;
import game.Field;

public class ArPlayer extends Player
{

    private Algorithm algorithm;

    private IndicatorSet indicatorSet;

    public ArPlayer(IndicatorSet indicatorSet)
    {
        super("Computer");

        this.indicatorSet = indicatorSet;
    }

    @Override
    public void setController(Controller controller) throws IllegalGamePlayerException
    {
        super.setController(controller);

        this.algorithm = new Algorithm(controller.getGame().getBoard(), this.indicatorSet, this);
        this.algorithm.setGame(controller.getGame());
    }

    public Integer getMove() throws Exception
    {
        Long tmp = System.currentTimeMillis();
        Integer move = this.algorithm.getMove();

        System.out.println("Calculation time: " + (System.currentTimeMillis() - tmp) + " ms");
        System.out.println("Move: x-" + PositionHelper.getX(move) + ", y-" + PositionHelper.getY(move));

        return move;
    }

}
