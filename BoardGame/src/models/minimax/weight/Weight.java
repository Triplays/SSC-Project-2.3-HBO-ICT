package models.minimax.weight;

import models.game.Field;

public abstract class Weight
{

    protected Double indicator;

    protected Field[] board;

    public Weight(Field[] board)
    {
        this.board = board;
    }

    public void setIndicator(Double indicator) {
        this.indicator = indicator;
    }
}
