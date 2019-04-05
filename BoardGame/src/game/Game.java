package game;

import display.Display;
import exceptions.IllegalGamePlayerException;
import exceptions.IllegalMoveException;
import player.Player;
import ruleset.Ruleset;

import java.util.ArrayList;
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

    private String gameName;

    private Random random = new Random();

    public Game(String name, int boardSize, Ruleset ruleset, Display display) {
        this.gameName = name;
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
        currentPlayer = players[0];
        currentPlayer.notifyPlayer();
    }

    /**
     * Registers a Player to this Game, and assign a color to the Player
     * @param player the Player to be registered to this Game
     * @throws IllegalGamePlayerException if there are alreadt two Player Objects registered to this Game
     */
    public void register(Player player) throws IllegalGamePlayerException {
        if (player.getColor() == Field.BLACK && players[0] == null) {
            players[0] = player;
        } else if (player.getColor() == Field.WHITE && players[1] == null) {
            players[1] = player;
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
        for (Integer integer : result) board[integer] = player.getColor();
        display.update(board);
        switchPlayers();
        switch(ruleset.checkWinCondition(board, currentPlayer.getColor())) {
            case SWAP:
                currentPlayer.notifyPlayer();
                break;
            case STAY:
                switchPlayers();
                currentPlayer.notifyPlayer();
                break;
            case WINWHITE:
                endGame(players[0].getName());
                break;
            case WINBLACK:
                endGame(players[1].getName());
                break;
            case DRAW:
                // TODO: Proper implementation of draw
                endGame("Nobody");
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


    public int giveMove(Field field) {
        int[] moves = ruleset.allLegalMoves(board, field, boardSize);
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < boardSize*boardSize; i++) {
            if (moves[i] != 0) temp.add(i);
        }
        return temp.get(random.nextInt(temp.size()));
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

    private void endGame(String winner) {
        System.out.println(winner + " has won!");
    }

    public Display getDisplay() {
        return display;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getGameName() { return gameName; }

}
