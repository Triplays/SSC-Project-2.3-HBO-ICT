package game;

import display.Display;
import exceptions.IllegalMoveException;
import player.Player;
import ruleset.Ruleset;

public class Game {

    private int[][] board;
    private int boardSize;

    private Player[] players;

    private Ruleset ruleset;
    private Player currentPlayer;

    private int lastSet;
    private Display display;
    private boolean started;

    public Game(int boardSize, Ruleset ruleset, Display display) {
        this.board = new int[boardSize][boardSize];
        this.boardSize = boardSize;
        this.ruleset = ruleset;
        this.display = display;

        players = new Player[2];
    }

    public void start() {
        started = true;
        currentPlayer = players[0];
    }

    public void register(Player player) {
        if (players[0] == null) {
            players[0] = player;
            player.setId(1);
        } else if (players[1] == null) {
            players[1] = player;
            player.setId(2);
        } else {
            // TODO: Handle player overflow
        }
    }

    public void move(Player player, int x, int y) throws IllegalMoveException {
        if (!started) {
            throw new IllegalMoveException("Game has not started yet");
        }
        if (player != currentPlayer) {
            throw new IllegalMoveException("It was not your turn");
        }
        if (!ruleset.legalMove(board, x, y)) {
            throw new IllegalMoveException("Illegal move");
        }

        board[x][y] = player.getId();
        lastSet = x * boardSize + y;
        display.update(board);
        switch(ruleset.gameEnded(board)) {
            case 0:
                switchPlayers();
                break;
            case 1:
                endGame(players[0]);
                break;
            case 2:
                endGame(players[1]);
                break;
            case -1:
                endGame(new Player("Nobody"));
                break;
        }
    }

    public void move(Player player, int target) throws IllegalMoveException {
        move(player, Math.floorDiv(target, boardSize), target % boardSize);
    }

    private void switchPlayers() {
        currentPlayer = currentPlayer == players[0] ? players[1] : players[0];
        currentPlayer.updateTurn(lastSet);
    }

    private void endGame(Player winner) {
        System.out.println(winner.getName() + " has won!");
    }

    public Display getDisplay() {
        return display;
    }
}
