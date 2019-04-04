package game;

import display.TicTacToeDisplay;
import ruleset.TicTacToeRuleset;

public class TicTacToeGame extends Game {

    public TicTacToeGame() {
        super("TicTactToe", 3, new TicTacToeRuleset(), new TicTacToeDisplay());
    }
}
