package algorithm;

import helper.PositionHelper;
import config.IndicatorSet;
import game.Field;
import game.Game;
import player.Player;
import ruleset.Ruleset;

import java.util.ArrayList;
import java.util.function.Function;

public class Algorithm
{

    private Game game;

    private Player me;

    private Ruleset ruleset;

    private Integer bestNextPosition;

    private IndicatorSet indicatorSet;

    private ArrayList<Function<Integer, Double>> weightStatements = new ArrayList<>();

    public Algorithm(Game game, IndicatorSet indicatorSet, Player me)
    {
        this.game = game;
        this.me = me;

        this.ruleset = this.game.getRuleset();

        this.indicatorSet = indicatorSet;
        this.applyAlgorithms(indicatorSet);
    }

//    public Integer simulate(Integer depth) throws Exception
//    {
//        Player turnPlayer = this.me;
//
//        Long tmp;
//
//        while (! this.game.isFull()) {
//
//            System.out.println("-----------------------");
//
//            tmp = System.currentTimeMillis();
//
//            Double score = this.brainstorm(turnPlayer, depth);
//            this.ruler.play(turnPlayer, this.bestNextPosition);
//
//            System.out.println("Calculation time: " + (System.currentTimeMillis() - tmp) + " ms");
//
//            System.out.println("Player " + (turnPlayer == BoardPosition.Owner.White ? "White" : "Black") + " did play position " + this.bestNextPosition.getX() + ":" + this.bestNextPosition.getY() + " (" + score + ").");
//            System.out.println(turnPlayer + " score: " + this.board.getPositionsAmount(turnPlayer));
//            System.out.println(this.ruler.getOpponent(turnPlayer) + " score: " + this.board.getPositionsAmount(this.ruler.getOpponent(turnPlayer)));
//
//            turnPlayer = this.ruler.getOpponent(turnPlayer);
//
//        }
//
//        return this.board.getPositionsAmount(this.me) - this.board.getPositionsAmount(this.ruler.getOpponent(this.me));
//    }

    public Double brainstorm(Field color, Integer depth) throws Exception
    {
        int[] availableMoves = this.ruleset.allLegalMoves(this.game.getBoard(), color, 8);

        System.out.println("Available moves:");
        for (int i = 0; i < availableMoves.length; i++) {
            if (availableMoves[i] == 0) {
                continue;
            }

            System.out.println("x:" + PositionHelper.getX(availableMoves[i]) + ", y:" + PositionHelper.getY(availableMoves[i]));
        }

        if (availableMoves.length == 0) {
            if (depth > 0) {
                Algorithm ar = new Algorithm(this.game.getClone(), this.indicatorSet, this.game.getOpponent(this.me));
                return -ar.brainstorm(color == Field.BLACK ? Field.WHITE : Field.BLACK, depth-1);
            }

            return 0.0;
        }

        Double bestScore = -10000000000.0;
        Integer bestMove = availableMoves[0];

        for (int i = 0; i < availableMoves.length; i++) {
            if (availableMoves[i] == 0) {
                continue;
            }

            Algorithm ar = new Algorithm(this.game.getClone(), this.indicatorSet, this.game.getOpponent(this.me));

            Integer newPositionsAmount = this.game.move(this.game.getPlayer(color), availableMoves[i]);

            Double score = this.calculateWeight(availableMoves[i]) * newPositionsAmount;

            if (depth > 0) {
                score -= ar.brainstorm(color == Field.BLACK ? Field.WHITE : Field.BLACK, depth-1);
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = availableMoves[i];
            }
        }

        this.bestNextPosition = bestMove;

        return bestScore;
    }

    public int getMove(Field color, int depth) throws Exception
    {
        this.brainstorm(color, depth);
        return this.bestNextPosition;
    }

    private Double calculateWeight(Integer field)
    {
        Double weight = 0.0;

        for (int i = 0; i < weightStatements.size(); i++) {
            weight = weight * weightStatements.get(i).apply(field);
        }

        return weight;
    }

    public void addWeightStatement(Function<Integer, Double> weightStatement)
    {
        this.weightStatements.add(weightStatement);
    }

    private void applyAlgorithms(IndicatorSet indicatorSet)
    {

        // Line Algorithm

        LineWeight lineWeight = new LineWeight(this.game, this.me);

        lineWeight.setIndicator(indicatorSet.getLineIndicator());

        this.addWeightStatement(lineWeight::execute);


        // Corner Algorithm

        CornerWeight cornerWeight = new CornerWeight(this.game, this.me);

        cornerWeight.setIndicator(indicatorSet.getCornerIndicator());

        this.addWeightStatement(cornerWeight::execute);


        // Border First Algorithm

        BorderFirstWeight borderFirstWeight = new BorderFirstWeight(this.game, this.me);

        borderFirstWeight.setIndicator(indicatorSet.getBorderFirstIndicator());

        this.addWeightStatement(borderFirstWeight::execute);

    }
}

