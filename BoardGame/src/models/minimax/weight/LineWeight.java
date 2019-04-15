package models.minimax.weight;

import models.helper.PositionHelper;
import models.game.Field;

import java.util.function.Function;

public class LineWeight extends Weight
{

    private Function<Integer, Boolean> hasFrom;
    private Function<Integer, Integer> from;
    private Function<Integer, Boolean> inversedHasFrom;
    private Function<Integer, Integer> inversedFrom;

    public LineWeight(Field[] board)
    {
        super(board);
    }

    public void setIndicator(Double indicator)
    {
        this.indicator = indicator;
    }

    public Double execute(int field)
    {
        Double score = 1.0;

        // Left

        this.hasFrom = PositionHelper::hasLeftFrom;
        this.inversedHasFrom = PositionHelper::hasRightFrom;

        this.from = PositionHelper::leftFrom;
        this.inversedFrom = PositionHelper::rightFrom;

        score = score * this.doExecute(field);

        // right

        this.hasFrom = PositionHelper::hasRightFrom;
        this.inversedHasFrom = PositionHelper::hasLeftFrom;

        this.from = PositionHelper::rightFrom;
        this.inversedFrom = PositionHelper::leftFrom;

        score = score * this.doExecute(field);

        // up

        this.hasFrom = PositionHelper::hasUpFrom;
        this.inversedHasFrom = PositionHelper::hasDownFrom;

        this.from = PositionHelper::upFrom;
        this.inversedFrom = PositionHelper::downFrom;

        score = score * this.doExecute(field);

        // down

        this.hasFrom = PositionHelper::hasDownFrom;
        this.inversedHasFrom = PositionHelper::hasUpFrom;

        this.from = PositionHelper::downFrom;
        this.inversedFrom = PositionHelper::upFrom;

        score = score * this.doExecute(field);

        // upper left

        this.hasFrom = PositionHelper::hasUpperLeftFrom;
        this.inversedHasFrom = PositionHelper::hasDownRightFrom;

        this.from = PositionHelper::upperLeftFrom;
        this.inversedFrom = PositionHelper::downRightFrom;

        score = score * this.doExecute(field);

        // upper right

        this.hasFrom = PositionHelper::hasUpperRightFrom;
        this.inversedHasFrom = PositionHelper::hasDownLeftFrom;

        this.from = PositionHelper::upperRightFrom;
        this.inversedFrom = PositionHelper::downLeftFrom;

        score = score * this.doExecute(field);

        // down left

        this.hasFrom = PositionHelper::hasDownLeftFrom;
        this.inversedHasFrom = PositionHelper::hasUpperRightFrom;

        this.from = PositionHelper::downLeftFrom;
        this.inversedFrom = PositionHelper::upperRightFrom;

        score = score * this.doExecute(field);

        // down right

        this.hasFrom = PositionHelper::hasDownRightFrom;
        this.inversedHasFrom = PositionHelper::hasUpperLeftFrom;

        this.from = PositionHelper::downRightFrom;
        this.inversedFrom = PositionHelper::upperLeftFrom;

        score = score * this.doExecute(field);

        return score;
    }

    private Double doExecute(int field)
    {
        Field me = this.board[field];
        Field opponent = (me == Field.BLACK ? Field.WHITE : Field.BLACK);

        int pos = field;
        Field above = Field.EMPTY;
        Field below = Field.EMPTY;

        while (this.hasFrom.apply(pos)) {
            try {
                pos = this.from.apply(pos);
                above = this.board[pos];

                if (above == me) {
                    continue;
                }

                break;

            } catch (Exception e) {}
        }

        pos = field;
        while (this.inversedHasFrom.apply(pos)) {
            try {
                pos = this.inversedFrom.apply(pos);
                below = this.board[pos];

                if (below == me) {
                    continue;
                }

                break;

            } catch (Exception e) {}
        }

        // When we are surrounded by the opponent, its good because we cant get beaten here
        if (above == below) {
            return this.indicator;
        }

        // When we are surrounded by the opponent on one side, its bad because they can take this over
        if (above == opponent || below == opponent) {
            return 1.0 / this.indicator;
        }

        return 1.0;
    }

}
