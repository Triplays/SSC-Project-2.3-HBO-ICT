package models.minimax.weight;

import models.game.Field;
import models.helper.PositionHelper;

public class CornerWeight extends Weight
{

    public CornerWeight(Field[] board)
    {
        super(board);
    }

    public void setIndicator(Double indicator)
    {
        this.indicator = indicator;
    }

    public Double execute(Integer field)
    {
        return 1.0;
    }
}
