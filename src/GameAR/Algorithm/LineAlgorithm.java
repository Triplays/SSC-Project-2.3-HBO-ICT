package GameAR.Algorithm;

import GameAR.Engine.Board;
import GameAR.Engine.BoardPosition;

import java.util.function.Function;

public class LineAlgorithm
{

    private Double indicator;

    private Board board;

    private BoardPosition.Owner opponent;
    private BoardPosition.Owner me;

    private Function<BoardPosition, Boolean> hasFrom;
    private Function<BoardPosition, BoardPosition> from;
    private Function<BoardPosition, Boolean> inversedHasFrom;
    private Function<BoardPosition, BoardPosition> inversedFrom;

    public LineAlgorithm(Board board, BoardPosition.Owner me) {
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
        Double score = 1.0;

        // Left

        this.hasFrom = this.board::hasLeftFrom;
        this.inversedHasFrom = this.board::hasRightFrom;

        this.from = (p) -> { try { return this.board.leftFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.rightFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        // right

        this.hasFrom = this.board::hasRightFrom;
        this.inversedHasFrom = this.board::hasLeftFrom;

        this.from = (p) -> { try { return this.board.rightFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.leftFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        // up

        this.hasFrom = this.board::hasUpFrom;
        this.inversedHasFrom = this.board::hasDownFrom;

        this.from = (p) -> { try { return this.board.upFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.downFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        // down

        this.hasFrom = this.board::hasDownFrom;
        this.inversedHasFrom = this.board::hasUpFrom;

        this.from = (p) -> { try { return this.board.downFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.upFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        // upper left

        this.hasFrom = this.board::hasUpperLeftFrom;
        this.inversedHasFrom = this.board::hasDownRightFrom;

        this.from = (p) -> { try { return this.board.upperLeftFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.downRightFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        // upper right

        this.hasFrom = this.board::hasUpperRightFrom;
        this.inversedHasFrom = this.board::hasDownLeftFrom;

        this.from = (p) -> { try { return this.board.upperRightFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.downLeftFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        // down left

        this.hasFrom = this.board::hasDownLeftFrom;
        this.inversedHasFrom = this.board::hasUpperRightFrom;

        this.from = (p) -> { try { return this.board.downLeftFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.upperRightFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        // down right

        this.hasFrom = this.board::hasDownRightFrom;
        this.inversedHasFrom = this.board::hasUpperLeftFrom;

        this.from = (p) -> { try { return this.board.downRightFrom(p); } catch (Exception e) { return null; } };;
        this.inversedFrom = (p) -> { try { return this.board.upperLeftFrom(p); } catch (Exception e) { return null; } };

        score = score * this.doExecute(boardPosition);

        return score;
    }

    private Double doExecute(BoardPosition boardPosition)
    {
        Boolean valid = true;

        BoardPosition pos = boardPosition;
        while(this.hasFrom.apply(pos)) {
            pos = this.from.apply(pos);

            if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                break;
            }

            if (! valid) {
                if (pos.getOwner() == this.me) {
                    valid = true;
                    break;
                }
            }

            if (pos.getOwner() == this.opponent) {
                valid = false;
            }
        }

        if (! valid) {
            while (this.inversedHasFrom.apply(pos)) {
                pos = this.inversedFrom.apply(pos);

                if (pos.getOwner() == this.opponent) {
                    break;
                }

                if (pos.getOwner() == BoardPosition.Owner.Unowned) {
                    return this.indicator;
                }
            }
        }

        return 1.0;
    }

}
