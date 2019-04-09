package models.algorithm;

import models.exceptions.IllegalMoveException;
import models.helper.PositionHelper;
import models.config.IndicatorSet;
import models.game.Field;
import models.game.Game;
import models.player.Player;
import models.ruleset.Ruleset;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;

public class Algorithm
{

    private static Game game;

    private static Ruleset ruleset;

    private Field[] board;

    private Player me;

    private Integer bestNextPosition;

    private IndicatorSet indicatorSet;

    private ArrayList<Function<Integer, Double>> weightStatements = new ArrayList<>();

    public Algorithm(Field[] board, IndicatorSet indicatorSet, Player me)
    {
        this.board = board;
        this.me = me;

        this.indicatorSet = indicatorSet;
        this.applyAlgorithms(indicatorSet);
    }

    public void setGame(Game game)
    {
        this.game = game;
        this.ruleset = game.getGameInfo().ruleset;
    }

    public Double brainstorm(Field color, Integer depth) throws Exception
    {
        Field[] playGround = this.getBoard().clone();

        int[] availableMoves = Algorithm.ruleset.allLegalMoves(this.board, color);

        if (availableMoves.length == 0) {
            if (depth > 0) {
                Algorithm ar = new Algorithm(playGround, this.indicatorSet, Algorithm.game.getOpponent(this.me));
                return -ar.brainstorm(color == Field.BLACK ? Field.WHITE : Field.BLACK, depth-1);
            }

            return 0.0;
        }

        Double bestScore = -10000000000.0;

        Integer bestMove = 0;

        for (int i = 0; i < availableMoves.length; i++) {
            if (availableMoves[i] == 0) {
                continue;
            }

            bestMove = i;
            break;
        }

        for (int i = 0; i < availableMoves.length; i++) {
            if (availableMoves[i] == 0) {
                continue;
            }

            HashSet<Integer> result = Algorithm.ruleset.legalMove(this.board, color, i);

            if (result.size() == 0) {
                continue;
            }

            for (Integer integer : result) playGround[integer] = color;

            Double score = this.calculateWeight(i) * result.size();

            Algorithm ar = new Algorithm(playGround, this.indicatorSet, Algorithm.game.getOpponent(this.me));

            if (depth > 0) {
                score -= ar.brainstorm(color == Field.BLACK ? Field.WHITE : Field.BLACK, depth-1);
            }

            if (score > bestScore) {
                bestScore = score;
                bestMove = i;
            }
        }

        this.bestNextPosition = bestMove;

        return bestScore;
    }

    public int getMove() throws Exception
    {
        this.brainstorm(this.me.getColor(), this.indicatorSet.getDepth());
        return this.bestNextPosition;
    }

    private Double calculateWeight(Integer field)
    {
        Double weight = 1.0;

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

        LineWeight lineWeight = new LineWeight(this, this.me);

        lineWeight.setIndicator(indicatorSet.getLineIndicator());

        this.addWeightStatement(lineWeight::execute);


        // Corner Algorithm

        CornerWeight cornerWeight = new CornerWeight(this, this.me);

        cornerWeight.setIndicator(indicatorSet.getCornerIndicator());

        this.addWeightStatement(cornerWeight::execute);


        // Border First Algorithm

        BorderFirstWeight borderFirstWeight = new BorderFirstWeight(this, this.me);

        borderFirstWeight.setIndicator(indicatorSet.getBorderFirstIndicator());

        this.addWeightStatement(borderFirstWeight::execute);

    }

    public Field[] getBoard()
    {
        return this.board;
    }

    public Game getGame()
    {
        return Algorithm.game;
    }

}

