package controller;

import game.Game;
import game.ReversiGame;
import game.TicTacToeGame;
import player.Player;

import java.util.Scanner;

public class LocalTicTacToeController implements Runnable {
    private Player player1;
    private Player player2;
    private Game game;
    private boolean active = true;

    public LocalTicTacToeController() {
        this.game = new TicTacToeGame();
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
        try {
            player1.setGame(game);
            player2.setGame(game);

            game.start();

            Scanner scanner = new Scanner(System.in);
            while (active) {
                game.printAllMoves(game.getCurrentPlayer().getColor());
                game.getCurrentPlayer().move(scanner.nextInt());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
