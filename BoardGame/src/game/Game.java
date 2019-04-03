package game;

import display.Display;
import exceptions.IllegalGamePlayerException;
import exceptions.IllegalMoveException;
import player.Player;
import ruleset.Ruleset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Game {

    private Field[] board;
    private int boardSize;

    private Ruleset ruleset;
    private Player currentPlayer;
    private Player[] players;

    private Display display;
    private boolean started;

    private Random random = new Random();

    public Game(int boardSize, Ruleset ruleset, Display display) {
        this.board = new Field[boardSize*boardSize];
        Arrays.fill(board, Field.EMPTY);
        this.boardSize = boardSize;
        this.ruleset = ruleset;
        this.display = display;

        players = new Player[2];
    }

    /**
     * Start the game and notify the first player
     */
    public void start() {
        started = true;
        currentPlayer = players[random.nextInt(2)];
        //currentPlayer = players[0];
        currentPlayer.notifyPlayer();
    }

    /**
     * Registers a Player to this Game, and assign a color to the Player
     * @param player the Player to be registered to this Game
     * @throws IllegalGamePlayerException if there are alreadt two Player Objects registered to this Game
     */
    public void register(Player player) throws IllegalGamePlayerException {
        if (players[0] == null) {
            players[0] = player;
            player.setColor(Field.WHITE);
        } else if (players[1] == null) {
            players[1] = player;
            player.setColor(Field.BLACK);
        } else {
            throw new IllegalGamePlayerException("There are already two players registered to this game.");
        }
    }

    /**
     * Deregisters a Player from this Game
     * @param player the Player to deregister from this Game
     * @throws IllegalGamePlayerException if the Player Object was not registered to this Game
     */
    public void deregister(Player player) throws IllegalGamePlayerException {
        if (players[0] == player) {
            players[0] = null;
        } else if (players[1] == player) {
            players[1] = null;
        } else {
            throw new IllegalGamePlayerException("Unknown player");
        }
    }

    /**
     * Attempt to move to the given position
     * @param player the Player that performs the move
     * @param target the target location on the board
     * @throws IllegalMoveException when the Game has not started, when it was not your turn, when the move was illegal
     */
    public void move(Player player, int target) throws IllegalMoveException {
        if (!started) {
            throw new IllegalMoveException("Game has not started yet");
        }
        if (player != currentPlayer) {
            throw new IllegalMoveException("It was not your turn");
        }
        HashSet<Integer> result = ruleset.legalMove(board, player.getColor(), target);
        if (result.size() == 0) {
            throw new IllegalMoveException("Illegal move");
        }
        for (Integer integer : result) System.out.println("Taken field " + integer);
        for (Integer integer : result) board[integer] = player.getColor();
        display.update(board);
        switchPlayers();
        switch(ruleset.checkWinCondition(board, player.getColor())) {
            case SWAP:
                currentPlayer.notifyPlayer();
                break;
            case STAY:
                switchPlayers();
                currentPlayer.notifyPlayer();
                break;
            case WINWHITE:
                endGame(players[0]);
                break;
            case WINBLACK:
                endGame(players[1]);
                break;
            case DRAW:
                // TODO: Proper implementation of draw
                endGame(new Player("Nobody"));
                break;
            default:
                break;
        }
    }

    /**
     * Force an index on the board to the given Field. Performs no validity checks. Useful for initiation
     * @param field the Field value to be set
     * @param target the target location on the board
     */
    public void put(Field field, int target) {
        board[target] = field;
        display.update(board);
    }

    public void printAllMoves(Field field) {
        int[] moves = ruleset.allLegalMoves(board, field, boardSize);
        for (int i = 0; i < boardSize*boardSize; i++) {
            if (moves[i] != 0) System.out.println("Index " + i + " captures " + moves[i]);
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
