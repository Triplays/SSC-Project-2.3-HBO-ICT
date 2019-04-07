package helper;

public class PositionHelper
{

    public static Boolean hasLeftFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);

        return x > 0;
    }

    public static Boolean hasRightFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);

        return x < 7;
    }


    public static Boolean hasUpFrom(Integer field)
    {
        Integer y = PositionHelper.getY(field);

        return y > 0;
    }

    public static Boolean hasDownFrom(Integer field)
    {
        Integer y = PositionHelper.getY(field);

        return y < 7;
    }

    public static Boolean hasUpperLeftFrom(Integer field)
    {
        return PositionHelper.hasUpFrom(field) && PositionHelper.hasLeftFrom(field);
    }

    public static Boolean hasUpperRightFrom(Integer field)
    {
        return PositionHelper.hasUpFrom(field) && PositionHelper.hasRightFrom(field);
    }

    public static Boolean hasDownLeftFrom(Integer field)
    {
        return PositionHelper.hasDownFrom(field) && PositionHelper.hasLeftFrom(field);
    }

    public static Boolean hasDownRightFrom(Integer field)
    {
        return PositionHelper.hasDownFrom(field) && PositionHelper.hasRightFrom(field);
    }

    public static Integer leftFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        x--;

        return (y * 8) + x;
    }

    public static Integer rightFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        x++;

        return (y * 8) + x;
    }

    public static Integer upFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        y--;

        return (y * 8) + x;
    }

    public static Integer downFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        y++;

        return (y * 8) + x;
    }

    public static Integer upperLeftFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        x--;
        y--;

        return (y * 8) + x;
    }

    public static Integer upperRightFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        x++;
        y--;

        return (y * 8) + x;
    }

    public static Integer downLeftFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        x--;
        y++;

        return (y * 8) + x;
    }

    public static Integer downRightFrom(Integer field)
    {
        Integer x = PositionHelper.getX(field);
        Integer y = PositionHelper.getY(field);

        x++;
        y++;

        return (y * 8) + x;
    }

    public static Integer getX(Integer field)
    {
        return field % 8;
    }

    public static Integer getY(Integer field)
    {
        return (int)Math.floor(field / 8);
    }

    public static Boolean isPosition(Integer field, Integer statementX, Integer statementY)
    {
        return PositionHelper.getX(field).equals(statementX) && PositionHelper.getY(field).equals(statementY);
    }

}
