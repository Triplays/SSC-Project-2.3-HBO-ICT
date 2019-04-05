package GameAR.Algorithm;

import GameAR.Engine.Board;
import GameAR.Engine.BoardPosition;
import GameAR.Config.IndicatorSet;
import GameAR.Engine.Ruler;

import java.util.ArrayList;

public class Algorithm
{

    private Board board;
    private Ruler ruler;

    private BoardPosition.Owner me = BoardPosition.Owner.Black;

    private BoardPosition bestNextPosition;

    private IndicatorSet indicatorSet;

    public Algorithm(Board board, IndicatorSet indicatorSet)
    {
        this.board = board;
        this.ruler = new Ruler(this.board);

        this.indicatorSet = indicatorSet;
        this.applyAlgorithms(indicatorSet);
    }

    public Integer simulate(int depth) throws Exception
    {
        this.board.get(4, 4).setOwner(BoardPosition.Owner.White);
        this.board.get(5, 5).setOwner(BoardPosition.Owner.White);

        this.board.get(4, 5).setOwner(BoardPosition.Owner.Black);
        this.board.get(5, 4).setOwner(BoardPosition.Owner.Black);

        BoardPosition.Owner turnPlayer = this.me;

        Long tmp;

        while (! this.board.isFull()) {

            System.out.println("-----------------------");

            tmp = System.currentTimeMillis();

            Double score = this.brainstorm(turnPlayer, depth);
            this.ruler.play(turnPlayer, this.bestNextPosition);

            System.out.println("Calculation time: " + (System.currentTimeMillis() - tmp) + " ms");

            System.out.println("Player " + (turnPlayer == BoardPosition.Owner.White ? "White" : "Black") + " did play position " + this.bestNextPosition.getX() + ":" + this.bestNextPosition.getY() + " (" + score + ").");
            System.out.println(turnPlayer + " score: " + this.board.getPositionsAmount(turnPlayer));
            System.out.println(this.ruler.getOpponent(turnPlayer) + " score: " + this.board.getPositionsAmount(this.ruler.getOpponent(turnPlayer)));

            turnPlayer = this.ruler.getOpponent(turnPlayer);

        }

        return this.board.getPositionsAmount(this.me) - this.board.getPositionsAmount(this.ruler.getOpponent(this.me));
    }

    public Double brainstorm(BoardPosition.Owner player, int depth) throws Exception
    {
        ArrayList<BoardPosition> availableMoves = this.ruler.getAvailableMoves(player);

        if (availableMoves.size() == 0) {
            if (depth > 0) {
                Algorithm ar = new Algorithm(this.board.clone(), this.indicatorSet);
                return -ar.brainstorm(ar.ruler.getOpponent(player), depth-1);
            }

            return 0.0;
        }

        Double bestScore = -10000000000.0;
        BoardPosition bestTurn = availableMoves.get(0);

        for (int i = 1; i < availableMoves.size(); i++) {
            Algorithm ar = new Algorithm(this.board.clone(), this.indicatorSet);

            BoardPosition pos = ar.board.get(availableMoves.get(i).getX(), availableMoves.get(i).getY());

            Integer newPositionsAmount = ar.ruler.play(player, pos);

            Double score = availableMoves.get(i).getWeight() * newPositionsAmount;

            if (depth > 0) {
                score -= ar.brainstorm(ar.ruler.getOpponent(player), depth-1);
            }

            if (score > bestScore) {
                bestScore = score;
                bestTurn = availableMoves.get(i);
            }
        }

        this.bestNextPosition = bestTurn;

        return bestScore;
    }

    private void applyAlgorithms(IndicatorSet indicatorSet)
    {

        // Line Algorithm

        LineAlgorithm lineAlgorithm = new LineAlgorithm(this.board, this.me);

        lineAlgorithm.setIndicator(indicatorSet.getLineIndicator());

        this.board.addWeightStatement(lineAlgorithm::execute);


        // Corner Algorithm

        CornerAlgorithm cornerAlgorithm = new CornerAlgorithm(this.board, this.me);

        cornerAlgorithm.setIndicator(indicatorSet.getCornerIndicator());

        this.board.addWeightStatement(cornerAlgorithm::execute);


        // Border First Algorithm

        BorderFirstAlgorithm borderFirstAlgorithm = new BorderFirstAlgorithm(this.board, this.me);

        borderFirstAlgorithm.setIndicator(indicatorSet.getBorderFirstIndicator());

        this.board.addWeightStatement(borderFirstAlgorithm::execute);

    }
}

