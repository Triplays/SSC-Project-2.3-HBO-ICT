package models.minimax.weight;

import models.game.Field;

public abstract class Weight
{

    protected Double indicator;

    protected Field[] board;

    protected Field me;

    protected Field opponent;

    public Weight(Field[] board, Field me)
    {
        this.board = board;
        this.me = me;
        this.opponent = me == Field.BLACK ? Field.WHITE : Field.BLACK;
    }

    public void setIndicator(Double indicator) {
        this.indicator = indicator;
    }
}
