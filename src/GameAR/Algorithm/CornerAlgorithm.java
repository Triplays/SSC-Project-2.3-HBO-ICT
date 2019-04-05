package GameAR.Algorithm;

import GameAR.Engine.Board;
import GameAR.Engine.BoardPosition;

public class CornerAlgorithm
{

    private Double indicator;

    private Board board;

    private BoardPosition.Owner opponent;
    private BoardPosition.Owner me;

    public CornerAlgorithm(Board board, BoardPosition.Owner me) {
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
        if (boardPosition.isPosition(1, 1) || boardPosition.isPosition(1, 8)
            || boardPosition.isPosition(8, 8) || boardPosition.isPosition(8, 1)) {
            return this.indicator;
        }

        return 1.0;
    }
}
