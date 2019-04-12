package models.helper;

public class PositionHelper
{

    public static Boolean hasLeftFrom(int field)
    {
        int x = PositionHelper.getX(field);

        return x > 0;
    }

    public static Boolean hasRightFrom(int field)
    {
        int x = PositionHelper.getX(field);

        return x < 7;
    }


    public static Boolean hasUpFrom(int field)
    {
        int y = PositionHelper.getY(field);

        return y > 0;
    }

    public static Boolean hasDownFrom(int field)
    {
        int y = PositionHelper.getY(field);

        return y < 7;
    }

    public static Boolean hasUpperLeftFrom(int field)
    {
        return PositionHelper.hasUpFrom(field) && PositionHelper.hasLeftFrom(field);
    }

    public static Boolean hasUpperRightFrom(int field)
    {
        return PositionHelper.hasUpFrom(field) && PositionHelper.hasRightFrom(field);
    }

    public static Boolean hasDownLeftFrom(int field)
    {
        return PositionHelper.hasDownFrom(field) && PositionHelper.hasLeftFrom(field);
    }

    public static Boolean hasDownRightFrom(int field)
    {
        return PositionHelper.hasDownFrom(field) && PositionHelper.hasRightFrom(field);
    }

    public static int leftFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        x--;

        return (y * 8) + x;
    }

    public static int rightFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        x++;

        return (y * 8) + x;
    }

    public static int upFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        y--;

        return (y * 8) + x;
    }

    public static int downFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        y++;

        return (y * 8) + x;
    }

    public static int upperLeftFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        x--;
        y--;

        return (y * 8) + x;
    }

    public static int upperRightFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        x++;
        y--;

        return (y * 8) + x;
    }

    public static int downLeftFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        x--;
        y++;

        return (y * 8) + x;
    }

    public static int downRightFrom(int field)
    {
        int x = PositionHelper.getX(field);
        int y = PositionHelper.getY(field);

        x++;
        y++;

        return (y * 8) + x;
    }

    public static int getX(int field)
    {
        return field % 8;
    }

    public static int getY(int field)
    {
        return (int)Math.floor(field / 8);
    }

    public static Boolean isPosition(int field, int statementX, int statementY)
    {
        return PositionHelper.getX(field) == statementX && PositionHelper.getY(field) == statementY;
    }

}
