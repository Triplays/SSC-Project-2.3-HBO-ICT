package board;

public class StandardBoard implements Board {

    int size;
    int[][] boardMatrix;

    public StandardBoard(int size) {
        this.size = size;
        boardMatrix = new int[size][size];
    }

    @Override
    public int get(int x, int y) {
        return boardMatrix[x][y];
    }

    @Override
    public int[][] getAll() {
        return boardMatrix;
    }

    @Override
    public void put(int x, int y, int value) {
        boardMatrix[x][y] = value;
    }

    @Override
    public int getSize() {
        return size;
    }
}
