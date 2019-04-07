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

    public ArPlayer(Field color)
    {
        super("Computer", color);
    }

    @Override
    public void setController(Controller controller) throws IllegalGamePlayerException
    {
        super.setController(controller);

        IndicatorSet indicatorSet = new IndicatorSet(5);
        indicatorSet.setLineIndicator(0.8);
        indicatorSet.setCornerIndicator(4.0);
        indicatorSet.setBorderFirstIndicator(2.0);

        this.algorithm = new Algorithm(controller.getGame(), indicatorSet, this);
    }

    public Integer getMove() throws Exception
    {
        Integer move = this.algorithm.getMove(this.getColor(), 5);

        System.out.println("Move: x-" + PositionHelper.getX(move) + ", y-" + PositionHelper.getY(move));

        return move;
    }

}
