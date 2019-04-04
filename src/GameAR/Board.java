package GameAR;

import java.util.ArrayList;
import java.util.function.Function;

public class Board implements Cloneable
{

    private int xAmount;
    private int yAmount;

    private ArrayList<BoardPosition> boardPositions = new ArrayList<>();
    private ArrayList<Function<BoardPosition, Double>> weightStatements = new ArrayList<>();

    public Board(int xAmount, int yAmount)
    {
        this.xAmount = xAmount;
        this.yAmount = yAmount;

        for (int x = 1; x <= xAmount; x++) {
            for (int y = 1; y <= yAmount; y++) {
                BoardPosition currentPosition = new BoardPosition(x, y);

                this.boardPositions.add(currentPosition);
            }
        }
    }

    public int getXAmount() {
        return xAmount;
    }

    public int getYAmount() {
        return yAmount;
    }

    public void setXAmount(int xAmount) {
        this.xAmount = xAmount;
    }

    public void setYAmount(int yAmount) {
        this.yAmount = yAmount;
    }

    public void setBoardPositions(ArrayList<BoardPosition> positions)
    {
        this.boardPositions = positions;
    }

    public void addWeightStatement(Function<BoardPosition, Double> weightStatement)
    {
        for (int i = 0; i < boardPositions.size(); i++) {
            boardPositions.get(i).attachWeightStatement(weightStatement);
        }
    }

    public BoardPosition get(int x, int y) throws Exception
    {
        for (int i = 0; i < boardPositions.size(); i++) {
            if (boardPositions.get(i).isPosition(x, y)) {
                return boardPositions.get(i);
            }
        }

        throw new Exception("No boardposition found");
    }

    public Boolean isFull()
    {
        for (int i = 0; i < boardPositions.size(); i++) {
            if (boardPositions.get(i).getOwner() == BoardPosition.Owner.Unowned) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<BoardPosition> getPositions(BoardPosition.Owner player)
    {
        ArrayList<BoardPosition> positions = new ArrayList<>();

        for (int i = 0; i < boardPositions.size(); i++) {
            if (boardPositions.get(i).getOwner() == player) {
                positions.add(boardPositions.get(i));
            }
        }

        return positions;
    }

    public int getPositionsAmount(BoardPosition.Owner player)
    {
        int positionsAmount = 0;

        for (int i = 0; i < boardPositions.size(); i++) {
            if (boardPositions.get(i).getOwner() == player) {
                positionsAmount++;
            }
        }

        return positionsAmount;
    }

    public boolean hasLeftFrom(BoardPosition boardPosition)
    {
        return boardPosition.getX() > 1;
    }

    public boolean hasRightFrom(BoardPosition boardPosition)
    {
        return boardPosition.getX() < this.xAmount;
    }

    public boolean hasUpperLeftFrom(BoardPosition boardPosition)
    {
        return boardPosition.getX() > 1 && boardPosition.getY() > 1;
    }

    public boolean hasUpperRightFrom(BoardPosition boardPosition)
    {
        return boardPosition.getX() < this.xAmount && boardPosition.getY() > 1;
    }

    public boolean hasUpFrom(BoardPosition boardPosition)
    {
        return boardPosition.getY() > 1;
    }

    public boolean hasDownLeftFrom(BoardPosition boardPosition)
    {
        return boardPosition.getX() > 1 && boardPosition.getY() < this.yAmount;
    }

    public boolean hasDownRightFrom(BoardPosition boardPosition)
    {
        return boardPosition.getX() < this.xAmount && boardPosition.getY() < this.yAmount;
    }

    public boolean hasDownFrom(BoardPosition boardPosition)
    {
        return boardPosition.getY() < this.yAmount;
    }

    public BoardPosition leftFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x-1, y);
    }

    public BoardPosition rightFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x+1, y);
    }

    public BoardPosition upperLeftFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x-1, y-1);
    }

    public BoardPosition upperRightFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x+1, y-1);
    }

    public BoardPosition upFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x, y-1);
    }

    public BoardPosition downLeftFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x-1, y+1);
    }

    public BoardPosition downRightFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x+1, y+1);
    }

    public BoardPosition downFrom(BoardPosition boardPosition) throws Exception
    {
        int x = boardPosition.getX();
        int y = boardPosition.getY();

        return this.get(x, y+1);
    }

    public Board clone() {
        Board board = new Board(0, 0);

        ArrayList<BoardPosition> boardPositions = new ArrayList<>();

        for (int i = 0; i < this.boardPositions.size(); i++) {
            boardPositions.add(this.boardPositions.get(i).clone());
        }

        board.setBoardPositions(boardPositions);

        board.setXAmount(this.xAmount);
        board.setYAmount(this.yAmount);

        for (int i = 0; i < this.weightStatements.size(); i++) {
            board.addWeightStatement(this.weightStatements.get(i));
        }

        return board;
    }

}
