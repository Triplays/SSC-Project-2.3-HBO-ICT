package game;

import display.Display;
import exceptions.IllegalMoveException;
import player.Player;
import ruleset.Ruleset;

import java.util.Arrays;

public class Game {

    private Field[][] board;
    private int boardSize;

    private Ruleset ruleset;
    private Player currentPlayer;
    private Player[] players;

    private Display display;
    private boolean started;

    public Game(int boardSize, Ruleset ruleset, Display display) {
        this.board = new Field[boardSize][boardSize];
        for (Field[] row : board) Arrays.fill(row, Field.EMPTY);
        System.out.println(board[1][1]);
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
            player.setColor(Field.WHITE);
        } else if (players[1] == null) {
            players[1] = player;
            player.setColor(Field.BLACK);
        } else {
            // TODO: Handle player overflow
        }
    }

    public void deregister(Player player) {
        if (players[0] == player) {
            players[0] = null;
        } else if (players[1] == player) {
            players[1] = null;
        } else {
            // TODO: Handle unknown player
        }
    }

    public void move(Player player, int x, int y) throws IllegalMoveException {
        if (!started) {
            throw new IllegalMoveException("Game has not started yet");
        }
        if (player != currentPlayer) {
            throw new IllegalMoveException("It was not your turn");
        }
        if (!ruleset.legalMove(board, boardSize, player.getColor(), x, y)) {
            throw new IllegalMoveException("Illegal move");
        }

        board[x][y] = player.getColor();
        display.update(board);
        switch(ruleset.gameEnded(board)) {
            case EMPTY:
                switchPlayers();
                break;
            case WHITE:
                endGame(players[0]);
                break;
            case BLACK:
                endGame(players[1]);
                break;
            case LEGAL:
                endGame(new Player("Nobody"));
                break;
        }
    }

    public void move(Player player, int target) throws IllegalMoveException {
        move(player, Math.floorDiv(target, boardSize), target % boardSize);
    }

    public void put(Field field, int x, int y) {
        board[x][y] = field;
        display.update(board);
    }

    public void printAllMoves() {
        for (Field[] row : ruleset.allLegalMoves(board, Field.WHITE, boardSize)) {
            System.out.println(Arrays.toString(row));
        }
    }

    private void switchPlayers() {
        currentPlayer = currentPlayer == players[0] ? players[1] : players[0];
    }

    private void endGame(Player winner) {
        System.out.println(winner.getName() + " has won!");
    }

    public Display getDisplay() {
        return display;
    }
}
