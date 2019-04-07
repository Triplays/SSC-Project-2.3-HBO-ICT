package algorithm;

import helper.PositionHelper;
import game.Game;
import player.Player;

public class CornerWeight
{

    private Double indicator;

    private Game game;

    private Player me;

    public CornerWeight(Game game, Player me)
    {
        this.game = game;
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
