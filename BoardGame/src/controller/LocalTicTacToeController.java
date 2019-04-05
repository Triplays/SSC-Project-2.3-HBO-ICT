package controller;

import game.Field;
import game.Game;
import game.TicTacToeGame;
import player.Player;

import java.util.Scanner;

public class LocalTicTacToeController implements Runnable, Controller {
    private Player player1;
    private Player player2;
    private Game game;
    private boolean active = true;

    public LocalTicTacToeController() {
        this.game = new TicTacToeGame();
        this.player1 = new Player("Player 1", this);
        this.player2 = new Player("Player 2", this);
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
                game.getCurrentPlayer().move(scanner.nextInt());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInput(Player player) {

    }

    @Override
    public void confirmation(boolean result) {

    }

    @Override
    public void matchStart(String opponentName, boolean myTurn) {

    }
}
