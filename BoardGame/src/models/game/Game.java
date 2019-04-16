package models.game;

import models.display.Display;
import models.exceptions.IllegalGamePlayerException;
import models.exceptions.IllegalMoveException;
import models.player.Player;

import java.util.HashSet;

public class Game implements Cloneable {

    private Field[] board;

    private GameInfo gameInfo;

    private Player currentPlayer;
    private Player[] players;

    private int scoreBlack;
    private int scoreWhite;

    private Display display;
    private boolean started;

    public Game(GameInfo gameInfo, Display display) {
        this.gameInfo = gameInfo;
        this.board = gameInfo.getInitialBoard();
        this.display = display;
        players = new Player[2];
    }

    /**
     * Start the game and notify the first player
     */
    public void start() {
        started = true;
        currentPlayer = players[0];
        updateScore();
        display.update(board, scoreBlack, scoreWhite, currentPlayer.getColor(), "");
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
            display.update(board, scoreBlack, scoreWhite, currentPlayer.getColor(), "Spel is nog niet begonnen");
            throw new IllegalMoveException("Game has not started yet");
        }
        if (player != currentPlayer) {
            display.update(board, scoreBlack, scoreWhite, currentPlayer.getColor(), "Het is niet jouw beurt");
            throw new IllegalMoveException("It was not your turn");
        }
        HashSet<Integer> result = gameInfo.ruleset.legalMove(board, player.getColor(), target);
        if (result.size() == 0) {
            display.update(board, scoreBlack, scoreWhite, currentPlayer.getColor(), "Ongeldige zet");
            throw new IllegalMoveException("Illegal move");
        }
        for (Integer integer : result) board[integer] = player.getColor();
        updateScore();
        switchPlayers();
        Gamestate gamestate = gameInfo.ruleset.checkGamestate(board, currentPlayer.getColor());
        switch(gamestate) {
            case SWAP:
                display.update(board, scoreBlack, scoreWhite, currentPlayer.getColor(), "");
                currentPlayer.notifyPlayer();
                break;
            case STAY:
                switchPlayers();
                display.update(board, scoreBlack, scoreWhite, currentPlayer.getColor(), "");
                currentPlayer.notifyPlayer();
                break;
            case WINWHITE:
                endGame(players[1].getName(), "");
                break;
            case WINBLACK:
                endGame(players[0].getName(), "");
                break;
            case DRAW:
                endGame("Niemand", "");
                break;
            default:
                break;
        }
    }

    private void updateScore() {
        scoreBlack = 0;
        scoreWhite = 0;
        for (Field field : board) {
            if(field == Field.BLACK) scoreBlack++;
            if(field == Field.WHITE) scoreWhite++;
        }
    }

    private void switchPlayers() {
        currentPlayer = currentPlayer == players[0] ? players[1] : players[0];
    }

    public void endGame(String winner, String comment) {
        String message = winner + " heeft gewonnen.";
        if (comment.length() > 0) message = message.concat(" Melding: " + comment);
        display.update(board, scoreBlack, scoreWhite, currentPlayer.getColor(), message);
    }

    public Field[] getBoard() { return board; }
    public GameInfo getGameInfo() { return gameInfo; }
}
