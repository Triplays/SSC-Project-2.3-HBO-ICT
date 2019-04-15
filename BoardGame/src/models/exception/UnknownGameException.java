package models.exception;

public class UnknownGameException extends Exception
{

    public UnknownGameException()
    {
        super("Unknown game.");
    }

}
