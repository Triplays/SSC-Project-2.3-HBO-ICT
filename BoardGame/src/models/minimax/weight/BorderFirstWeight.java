package models.minimax.weight;

import models.helper.PositionHelper;
import models.game.Field;

public class BorderFirstWeight extends Weight
{

    public BorderFirstWeight(Field[] board, Field me)
    {
        super(board, me);
    }

    public void setIndicator(Double indicator)
    {
        this.indicator = indicator;
    }

    public Double execute(Integer field)
    {
        if (PositionHelper.getX(field) == 0 || PositionHelper.getX(field) == 7) {
            return this.doExecuteVertical(field);
        }

        if (PositionHelper.getY(field) == 0 || PositionHelper.getY(field) == 7) {
            return this.doExecuteHorizontal(field);
        }

        return 1.0;
    }

    public Double doExecuteVertical(int field)
    {
        Boolean valid = true;
        Integer pos = field;
        while (PositionHelper.hasUpFrom(pos)) {
            try {
                pos = PositionHelper.upFrom(pos);

                if (this.board[pos] == Field.EMPTY) {
                    break;
                }

                if (this.board[pos] == this.opponent) {
                    pos = field;

                    while (PositionHelper.hasDownFrom(pos)) {
                        try {
                            pos = PositionHelper.downFrom(pos);

                            if (this.board[pos] == Field.EMPTY) {
                                break;
                            }

                            if (this.board[pos] == this.opponent) {
                                valid = false;
                            }

                        } catch (Exception e) {}
                    }

                    break;
                }

            } catch (Exception e) {}
        }

        if (! valid) {
            return 1.0;
        }

        return this.indicator;
    }

    public Double doExecuteHorizontal(Integer field)
    {
        Boolean valid = true;
        Integer pos = field;
        while (PositionHelper.hasLeftFrom(pos)) {
            try {
                pos = PositionHelper.leftFrom(pos);

                if (this.board[pos] == Field.EMPTY) {
                    break;
                }

                if (this.board[pos] == this.opponent) {
                    pos = field;

                    while (PositionHelper.hasRightFrom(pos)) {
                        try {
                            pos = PositionHelper.rightFrom(pos);

                            if (this.board[pos] == Field.EMPTY) {
                                break;
                            }

                            if (this.board[pos] == this.opponent) {
                                valid = false;
                            }

                        } catch (Exception e) {}
                    }

                    break;
                }

            } catch (Exception e) {}
        }

        if (! valid) {
            return 1.0;
        }

        return this.indicator;
    }
}
