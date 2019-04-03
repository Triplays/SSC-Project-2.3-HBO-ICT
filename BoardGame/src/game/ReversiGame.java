package game;

import display.ReversiDisplay;
import ruleset.ReversiRuleset;

public class ReversiGame extends Game {
    public ReversiGame() {
        super(8, new ReversiRuleset(), new ReversiDisplay());
        put(Field.WHITE, 27);
        put(Field.WHITE, 36);
        put(Field.BLACK, 28);
        put(Field.BLACK, 35);
    }
}
