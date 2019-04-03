package controller;

import game.Game;
import game.ReversiGame;
import player.Player;

import java.util.Scanner;

public class LocalReversiContoller {

    Player player1;
    Player player2;
    Game game;

    public LocalReversiContoller() {
        this.game = new ReversiGame();
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        player1.setGame(game);
        player2.setGame(game);

        game.start();

        game.printAllMoves();

        try {
            player1.move(4, 2);
            //player2.move(5);
            //player1.move(2, 1);
            //player2.move(4);
            //player1.move(2, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Game getGame() {
        return game;
    }
}
