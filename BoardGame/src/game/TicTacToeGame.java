package game;

import board.TicTacToeBoard;
import display.TicTacToeDisplay;
import player.Player;
import ruleset.TicTacToeRuleset;

public class TicTacToeGame extends Game {

    public TicTacToeGame(Player player1, Player player2) {
        super(new TicTacToeBoard(), new TicTacToeRuleset(), new TicTacToeDisplay(), player1, player2);
    }
}
