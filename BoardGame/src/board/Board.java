package board;

public interface Board {

    int get(int x, int y);
    int[][] getAll();
    void put(int x, int y, int value);
    int getSize();
}
