package models.config;

public class ReversiIndicatorSet extends IndicatorSet
{

    private Double lineIndicator;

    private Double cornerIndicator;

    private Double borderFirstIndicator;

    public ReversiIndicatorSet(Integer depth) {
        super(depth);
    }

    public Double getLineIndicator() {
        return lineIndicator;
    }

    public void setLineIndicator(Double lineIndicator)
    {
        this.lineIndicator = lineIndicator;
    }

    public Double getCornerIndicator()
    {
        return cornerIndicator;
    }

    public void setCornerIndicator(Double cornerIndicator)
    {
        this.cornerIndicator = cornerIndicator;
    }

    public Double getBorderFirstIndicator()
    {
        return borderFirstIndicator;
    }

    public void setBorderFirstIndicator(Double borderFirstIndicator)
    {
        this.borderFirstIndicator = borderFirstIndicator;
    }
}
