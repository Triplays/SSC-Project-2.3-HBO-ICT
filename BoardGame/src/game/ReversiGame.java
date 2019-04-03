package game;

import display.ReversiDisplay;
import ruleset.ReversiRuleset;

public class ReversiGame extends Game {
    public ReversiGame() {
        super(8, new ReversiRuleset(), new ReversiDisplay());
        put(Field.WHITE, 3, 3);
        put(Field.WHITE, 4, 4);
        put(Field.BLACK, 3, 4);
        put(Field.BLACK, 4, 3);
    }
}
