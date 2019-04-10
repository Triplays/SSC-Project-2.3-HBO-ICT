package models.minimax.weight;

import models.game.Field;
import models.helper.PositionHelper;

public class CornerWeight extends Weight
{

    public CornerWeight(Field[] board, Field me)
    {
        super(board, me);
    }

    public void setIndicator(Double indicator)
    {
        this.indicator = indicator;
    }

    public Double execute(Integer field)
    {
        if (PositionHelper.isPosition(field, 0, 0)) {
            return this.indicator;
        } else if(PositionHelper.isPosition(field, 1, 0)
                || PositionHelper.isPosition(field, 0, 1)) {
            return -this.indicator;
        }

        if (PositionHelper.isPosition(field, 0, 7)) {
            return this.indicator;
        } else if(PositionHelper.isPosition(field, 0, 6)
                || PositionHelper.isPosition(field, 1, 7)) {
            return -this.indicator;
        }

        if (PositionHelper.isPosition(field, 7, 7)) {
            return this.indicator;
        } else if(PositionHelper.isPosition(field, 7, 6)
                || PositionHelper.isPosition(field, 6, 7)) {
            return -this.indicator;
        }

        if (PositionHelper.isPosition(field, 7, 0)) {
            return this.indicator;
        } else if(PositionHelper.isPosition(field, 7, 1)
                || PositionHelper.isPosition(field, 6, 0)) {
            return -this.indicator;
        }

        return 1.0;
    }
}
