package models.minimax;

import models.config.ReversiIndicatorSet;
import models.minimax.weight.BorderFirstWeight;
import models.minimax.weight.CornerWeight;
import models.minimax.weight.LineWeight;
import models.config.IndicatorSet;
import models.game.Field;
import models.game.GameInfo;

import java.util.ArrayList;
import java.util.function.Function;

public class ReversiMinimax extends Minimax<ReversiIndicatorSet>
{

    protected ArrayList<Function<Integer, Double>> weightStatements = new ArrayList<>();

    private final int[] scorematrix = {
            30,-5,  5,  5,  5,  5, -5, 30,
            -5,-5,  0,  0,  0,  0, -5, -5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            5,  0,  2,  2,  2,  2,  0,  5,
            -5,-5,  0,  0,  0,  0, -5, -5,
            30,-5,  5,  5,  5,  5, -5, 30
    };

    public ReversiMinimax(Field field)
    {
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
            if(board[i] == Field.WHITE) white += (scorematrix[i] * weightMatrix[i]);
            if(board[i] == Field.BLACK) black += (scorematrix[i] * weightMatrix[i]);
        }

        return self == Field.BLACK ? (black - white) : (white - black);
    }

    protected Double getScore(Integer field)
    {
        Double weight = 1.0;

        for (int i = 0; i < weightStatements.size(); i++) {
            weight = weight * weightStatements.get(i.apply(field);
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

        LineWeight lineWeight = new LineWeight(board, this.self);

        lineWeight.setIndicator(indicatorSet.getLineIndicator());

        this.addWeightStatement(lineWeight::execute);


        // Corner Algorithm

        CornerWeight cornerWeight = new CornerWeight(board, this.self);

        cornerWeight.setIndicator(indicatorSet.getCornerIndicator());

        this.addWeightStatement(cornerWeight::execute);


        // Border First Algorithm

        BorderFirstWeight borderFirstWeight = new BorderFirstWeight(board, this.self);

        borderFirstWeight.setIndicator(indicatorSet.getBorderFirstIndicator());

        this.addWeightStatement(borderFirstWeight::execute);


        double[] weightMatrix = new double[64];

        for (int i = 0; i < board.length; i++) {
            weightMatrix[i] = this.getScore(i);
        }

        this.weightStatements.clear();

        return weightMatrix;

    }

}
