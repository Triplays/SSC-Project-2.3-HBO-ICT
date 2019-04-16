package models.game;

/**
 * Represents a field on a board. Can either be empty, filled by white, or filled by black.
 * Custom name can be passed in the constructor.
 */
public enum Field {
    EMPTY("Niemand"),
    WHITE("Wit"),
    BLACK("Zwart");

    public final String name;
    Field(String name) { this.name = name; }
}
