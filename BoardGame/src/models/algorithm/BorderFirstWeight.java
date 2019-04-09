package models.algorithm;

import models.helper.PositionHelper;
import models.game.Field;
import models.game.Game;
import models.player.Player;

public class BorderFirstWeight
{

    private Double indicator;

    private Algorithm algorithm;

    private Player me;

    public BorderFirstWeight(Algorithm algorithm, Player me)
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
        if (PositionHelper.getX(field) == 1 || PositionHelper.getX(field) == 8) {
            return this.doExecuteVertical(field);
        }

        if (PositionHelper.getY(field) == 1 || PositionHelper.getY(field) == 8) {
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

                if (this.algorithm.getBoard()[pos] == Field.EMPTY) {
                    break;
                }

                if (this.algorithm.getBoard()[pos] == algorithm.getGame().getOpponent(this.me).getColor()) {
                    pos = field;

                    while (PositionHelper.hasDownFrom(pos)) {
                        try {
                            pos = PositionHelper.downFrom(pos);

                            if (algorithm.getGame().getBoard()[pos] == Field.EMPTY) {
                                break;
                            }

                            if (algorithm.getGame().getBoard()[pos] == algorithm.getGame().getOpponent(this.me).getColor()) {
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

                if (algorithm.getBoard()[pos] == Field.EMPTY) {
                    break;
                }

                if (algorithm.getBoard()[pos] == algorithm.getGame().getOpponent(this.me).getColor()) {
                    pos = field;

                    while (PositionHelper.hasRightFrom(pos)) {
                        try {
                            pos = PositionHelper.rightFrom(pos);

                            if (algorithm.getBoard()[pos] == Field.EMPTY) {
                                break;
                            }

                            if (algorithm.getBoard()[pos] == algorithm.getGame().getOpponent(this.me).getColor()) {
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
