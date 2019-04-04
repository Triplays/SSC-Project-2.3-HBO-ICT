package GameAR;

public class BorderFirstAlgorithm
{

    private Double indicator;

    private Board board;

    private BoardPosition.Owner opponent;
    private BoardPosition.Owner me;

    public BorderFirstAlgorithm(Board board, BoardPosition.Owner me) {
        this.board = board;
        this.me = me;
        this.opponent = (me == BoardPosition.Owner.White ? BoardPosition.Owner.Black : BoardPosition.Owner.White);
    }

    public void setIndicator(Double indicator)
    {
        this.indicator = indicator;
    }

    public Double execute(BoardPosition boardPosition)
    {
        if (boardPosition.getX() == 1 || boardPosition.getX() == 8) {
            return this.doExecuteVertical(boardPosition);
        }

        if (boardPosition.getY() == 1 || boardPosition.getY() == 8) {
            return this.doExecuteHorizontal(boardPosition);
        }

        return 1.0;
    }

    public Double doExecuteVertical(BoardPosition boardPosition)
    {
        Boolean valid = true;
        BoardPosition pos = boardPosition;
        while (this.board.hasUpFrom(pos)) {
            try {
                pos = this.board.upFrom(pos);

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    break;
                }

                if (pos.getOwner() == this.opponent) {
                    pos = boardPosition;

                    while (this.board.hasDownFrom(pos)) {
                        try {
                            pos = this.board.downFrom(pos);

                            if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                                break;
                            }

                            if (pos.getOwner() == this.opponent) {
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

    public Double doExecuteHorizontal(BoardPosition boardPosition)
    {
        Boolean valid = true;
        BoardPosition pos = boardPosition;
        while (this.board.hasLeftFrom(pos)) {
            try {
                pos = this.board.leftFrom(pos);

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    break;
                }

                if (pos.getOwner() == this.opponent) {
                    pos = boardPosition;

                    while (this.board.hasRightFrom(pos)) {
                        try {
                            pos = this.board.rightFrom(pos);

                            if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                                break;
                            }

                            if (pos.getOwner() == this.opponent) {
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
