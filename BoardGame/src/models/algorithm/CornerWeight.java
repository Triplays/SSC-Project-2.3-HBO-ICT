package models.algorithm;

import models.helper.PositionHelper;
import models.game.Game;
import models.player.Player;

public class CornerWeight
{

    private Double indicator;

    private Algorithm algorithm;

    private Player me;

    public CornerWeight(Algorithm algorithm, Player me)
    {
        this.algorithm = algorithm;
        this.me = me;
    }

    public void setIndicator(Double indicator)
    {
        this.indicator = indicator;
    }

    public Double execute(Integer field)
    {
        if (PositionHelper.isPosition(field, 1, 1)
            || PositionHelper.isPosition(field, 1, 8)
            || PositionHelper.isPosition(field, 8, 8)
            || PositionHelper.isPosition(field, 8, 1)) {
            return this.indicator;
        }

        return 1.0;
    }
}
