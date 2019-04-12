package models.config;

public class ReversiIndicatorSet extends IndicatorSet
{

    private double lineIndicator;

    private double cornerIndicator;

    public ReversiIndicatorSet(Integer depth) {
        super(depth);
    }

    public Boolean hasLineIndicator()
    {
        return lineIndicator > 0;
    }

    public double getLineIndicator()
    {
        return lineIndicator;
    }

    public void setLineIndicator(double lineIndicator)
    {
        this.lineIndicator = lineIndicator;
    }

    public Boolean hasCornerIndicator()
    {
        return cornerIndicator > 0;
    }

    public double getCornerIndicator()
    {
        return cornerIndicator;
    }

    public void setCornerIndicator(double cornerIndicator)
    {
        this.cornerIndicator = cornerIndicator;
    }
}
