package algorithm;

import helper.PositionHelper;
import game.Field;
import game.Game;
import player.Player;

public class BorderFirstWeight
{

    private Double indicator;

    private Game game;

    private Player me;

    public BorderFirstWeight(Game game, Player me)
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

                if (this.game.getBoard()[pos] == Field.EMPTY) {
                    break;
                }

                if (this.game.getBoard()[pos] == this.game.getOpponent(this.me).getColor()) {
                    pos = field;

                    while (PositionHelper.hasDownFrom(pos)) {
                        try {
                            pos = PositionHelper.downFrom(pos);

                            if (this.game.getBoard()[pos] == Field.EMPTY) {
                                break;
                            }

                            if (this.game.getBoard()[pos] == this.game.getOpponent(this.me).getColor()) {
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

                if (this.game.getBoard()[pos] == Field.EMPTY) {
                    break;
                }

                if (this.game.getBoard()[pos] == this.game.getOpponent(this.me).getColor()) {
                    pos = field;

                    while (PositionHelper.hasRightFrom(pos)) {
                        try {
                            pos = PositionHelper.rightFrom(pos);

                            if (this.game.getBoard()[pos] == Field.EMPTY) {
                                break;
                            }

                            if (this.game.getBoard()[pos] == this.game.getOpponent(this.me).getColor()) {
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
