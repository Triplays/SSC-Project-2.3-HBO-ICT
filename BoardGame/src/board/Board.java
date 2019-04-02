package board;

public class Board {

    int size;
    int[][] boardMatrix;

    public Board(int size) {
        this.size = size;
        boardMatrix = new int[size][size];
    }

    public int get(int x, int y) {
        return boardMatrix[x][y];
    }
    public int[][] getAll() {
        return boardMatrix;
    }
    public void put(int x, int y, int value) {
        boardMatrix[x][y] = value;
    }
    public int getSize() { return size; }
}
