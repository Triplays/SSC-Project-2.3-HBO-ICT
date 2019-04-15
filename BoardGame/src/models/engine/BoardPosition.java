package models.engine;

import java.util.ArrayList;
import java.util.function.Function;

public class BoardPosition
{

    public enum Owner {
        Unowned,
        White,
        Black
    }

    private Owner owner = Owner.Unowned;

    private int x;
    private int y;

    private Double weight = 1.0;

    private ArrayList<Function<BoardPosition, Double>> weightStatements = new ArrayList<Function<BoardPosition, Double>>();

    public BoardPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean isPosition(int x, int y)
    {
        return this.x == x && this.y == y;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }
    public Owner getOwner()
    {
        return this.owner;
    }

    public void setOwner(Owner owner)
    {
        this.owner = owner;
    }

    public void attachWeightStatement(Function<BoardPosition, Double> weightStatement)
    {
        this.weightStatements.add(weightStatement);
    }

    public Double getWeight()
    {
        return this.calculateWeight();
    }

    private Double calculateWeight()
    {

        for (int i = 0; i < weightStatements.size(); i++) {
            this.weight = this.weight * weightStatements.get(i).apply(this);
        }

        return this.weight;
    }

    public BoardPosition clone()
    {
        BoardPosition clone = new BoardPosition(this.getX(), this.getY());
        clone.setOwner(this.getOwner());

        for (int i = 0; i < this.weightStatements.size(); i++) {
            clone.attachWeightStatement(this.weightStatements.get(i));
        }

        return clone;
    }
}
