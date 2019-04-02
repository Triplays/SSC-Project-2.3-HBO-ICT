package controller;

import game.Game;
import game.TicTacToeGame;
import player.Player;

public class LocalTicTacToeController {

    Player player1;
    Player player2;
    Game game;

    public LocalTicTacToeController() {
        this.game = new TicTacToeGame();
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        player1.setGame(game);
        player2.setGame(game);

        game.start();

        try {
            player1.move(2, 2);
            player2.move(5);
            player1.move(2, 1);
            player2.move(4);
            player1.move(2, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Game getGame() {
        return game;
    }
}
