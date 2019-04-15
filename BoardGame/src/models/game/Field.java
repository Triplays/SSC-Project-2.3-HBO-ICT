package models.game;

public enum Field {
    EMPTY("Leeg"),
    WHITE("Wit"),
    BLACK("Zwart");

    public final String name;
    Field(String name) { this.name = name; }
}
