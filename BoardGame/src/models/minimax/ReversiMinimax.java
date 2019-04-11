package models.minimax;

import models.config.ReversiIndicatorSet;
import models.minimax.weight.CornerWeight;
import models.minimax.weight.LineWeight;
import models.game.Field;
import models.game.GameInfo;

import java.util.ArrayList;
import java.util.function.Function;

public class ReversiMinimax extends Minimax<ReversiIndicatorSet>
{

    protected ArrayList<Function<Integer, Double>> weightStatements = new ArrayList<>();

    private final int[] scoreMatrix = {
            30,-5,  5,  5,  5,  5, -5, 30,
            -5,-5,  0,  0,  0,  0, -5, -5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            -5,-5,  0,  0,  0,  0, -5, -5,
            30,-5,  5,  5,  5,  5, -5, 30
    };

    private final int[] scoreMatrixTwo = {
            100,  -25,  25,  10,  10,   25,  -25,  100,
            -25,  -50,  -5,   0,   0,   -5,  -50,  -25,
            25,   -5,   10,   2,   2,   10,   -5,   25,
            10,    0,    2,   5,   5,    2,    0,   10,
            10,    0,    2,   5,   5,    2,    0,   10,
            25,   -5,   10,   2,   2,   10,   -5,   25,
            -25,  -50,  -5,   0,   0,   -5,  -50,  -25,
            100,  -25,  25,  10,  10,   25,  -25,  100
    };

    public ReversiMinimax(Field field){
        super(GameInfo.REVERSI, field);
    }

    @Override
    public void setIndicatorSet(ReversiIndicatorSet indicatorSet)
    {
        this.indicatorSet = indicatorSet;
    }

    @Override
    int calculateScore(Field[] board, Field self)
    {
        double[] weightMatrix = this.createWeightMatrix(board, this.indicatorSet);

        int white = 0;
        int black = 0;
        for (int i =0; i < board.length; i++) {
            boolean negative = scoreMatrixTwo[i] < 0;
            double score = scoreMatrixTwo[i] * (negative ? 1 / weightMatrix[i] : weightMatrix[i]);

            if(board[i] == Field.WHITE) white += score;
            if(board[i] == Field.BLACK) black += score;
        }

        return self == Field.BLACK ? (black - white) : (white - black);
    }

    protected Double getScore(Integer field)
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

    public double[] createWeightMatrix(Field[] board, ReversiIndicatorSet indicatorSet)
    {

        // Line Algorithm

        if (indicatorSet.hasLineIndicator()) {
            LineWeight lineWeight = new LineWeight(board);

            lineWeight.setIndicator(indicatorSet.getLineIndicator());

            this.addWeightStatement(lineWeight::execute);
        }

        // Corner Algorithm

        if (indicatorSet.hasCornerIndicator()) {
            CornerWeight cornerWeight = new CornerWeight(board);

            cornerWeight.setIndicator(indicatorSet.getCornerIndicator());

            this.addWeightStatement(cornerWeight::execute);
        }

        double[] weightMatrix = new double[64];

        for (int i = 0; i < board.length; i++) {
            weightMatrix[i] = this.getScore(i);
        }

        this.weightStatements.clear();

        return weightMatrix;

    }

}
