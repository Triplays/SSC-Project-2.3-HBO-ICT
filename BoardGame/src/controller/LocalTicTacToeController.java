package controller;

import game.Game;
import game.TicTacToeGame;
import player.Player;

public class LocalTicTacToeController {

    Player player1;
    Player player2;
    Game game;

    public LocalTicTacToeController() {
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        this.game = new TicTacToeGame(player1, player2);

        game.start();

        try {
            game.move(player1, 2, 2);
            game.move(player2, 1, 2);
            game.move(player1, 2, 1);
            game.move(player2, 1, 1);
            game.move(player1, 2, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Game getGame() {
        return game;
    }
}
