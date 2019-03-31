package game;

import board.Board;
import display.Display;
import exceptions.IllegalMoveException;
import player.Player;
import ruleset.Ruleset;

import java.util.Random;

public class Game {

    Board board;
    Ruleset ruleset;
    Player player1;
    Player player2;
    Player currentPlayer;
    boolean started = false;
    int lastSet;

    Display display;

    public Game(Board board, Ruleset ruleset, Display display, Player player1, Player player2) {
        this.board = board;
        this.ruleset = ruleset;
        this.player1 = player1;
        this.player1.setId(1);
        this.player2 = player2;
        this.player2.setId(2);
        this.display = display;
    }

    public void start() {
        started = true;
        //currentPlayer = new Random().nextInt(2) == 1 ? player1 : player2;
        currentPlayer = player1;
    }

    public void move(Player player, int x, int y) throws IllegalMoveException {
        if (started) {
            if (player == currentPlayer) {
                if(ruleset.legalMove(board.getAll(), x, y)) {
                    board.put(x, y, player.getId());
                    lastSet = x * board.getSize() + y;
                    switch(ruleset.gameEnded(board.getAll())) {
                        case 0:
                            switchPlayers();
                            break;
                        case 1:
                            endGame(player1);
                            break;
                        case 2:
                            endGame(player2);
                            break;
                        case -1:
                            endGame(new Player("Nobody"));
                            break;
                    }
                } else {
                    throw new IllegalMoveException("Illegal move");
                }
            } else {
                throw new IllegalMoveException("It was not your turn");
            }
        } else {
            throw new IllegalMoveException("Game has not started yet");
        }
    }

    private void switchPlayers() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
        currentPlayer.updateTurn(lastSet);
    }

    private void endGame(Player winner) {
        started = false;
        System.out.println(winner.getName() + " has won!");
    }

    public boolean isStarted() {
        return started;
    }

    public Display getDisplay() {
        return display;
    }
}
