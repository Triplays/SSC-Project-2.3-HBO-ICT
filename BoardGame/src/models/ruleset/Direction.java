package models.ruleset;

/**
 * Directions made for an eight by eight board.
 * Constructor takes the difference in index to reach that position on this size board.
 */
public enum Direction {
    TOP(-8) {           @Override public boolean limit(int target) { return target < 8; } },
    TOPRIGHT(-7) {      @Override public boolean limit(int target) { return target < 8 || target % 8 == 7; } },
    RIGHT(1) {          @Override public boolean limit(int target) { return target % 8 == 7; } },
    BOTTOMRIGHT(9) {    @Override public boolean limit(int target) { return target > 55 || target % 8 == 7; } },
    BOTTOM(8) {         @Override public boolean limit(int target) { return target > 55; } },
    BOTTOMLEFT(7) {     @Override public boolean limit(int target) { return target > 55 || target % 8 == 0; } },
    LEFT(-1) {          @Override public boolean limit(int target) { return target % 8 == 0; } },
    TOPLEFT(-9) {       @Override public boolean limit(int target) { return target < 8 || target % 8 == 0; } };

    public final int dir;
    Direction(int dir) { this.dir = dir; }

    /**
     * Determines whether the edge of the board has been reached in this direction.
     * @param target the target index to check
     * @return whether this index will leave the board when continued in its direction.
     */
    public abstract boolean limit(int target);
}
