package models.gamecontroller;

import models.display.Display;
import models.exceptions.IllegalGamePlayerException;
import models.exceptions.IllegalMoveException;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.LocalMinimaxPlayer;
import models.player.PhysicalPlayer;
import models.player.Player;

/**
 * Game controller to manage a game played locally.
 */
public class LocalGameController implements Runnable, GameController {

    private Player player;
    private Player opponent;
    private Player activePlayer;
    private Game game;
    private Display display;
    private boolean active = true;
    private boolean pending = false;
    private final Object awaitGame = new Object();
    private final Object awaitInput = new Object();
    private int input;

    /**
     * Constructor for a local computer versus computer game
     * @param gameInfo              Game information to be played
     * @param computerOneStrenght   Difficulty of computer 1
     * @param computerTwoStrength   Difficulty of computer 2
     */
    public LocalGameController(GameInfo gameInfo, int computerOneStrenght, int computerTwoStrength) {
        this.display = gameInfo.getDisplay(this);
        this.game = new Game(gameInfo, display);
        this.player = new LocalMinimaxPlayer("Computer niveau " + computerOneStrenght, Field.BLACK, this, computerOneStrenght);
        this.opponent = new LocalMinimaxPlayer("Computer niveau " + computerTwoStrength, Field.WHITE, this, computerTwoStrength);
    }

    /**
     * Constructor for a local player versus computer game
     * @param gameInfo          Game information to be played
     * @param playerName        Name of the player
     * @param color             Preferred color for the player
     * @param computerStrenght  Difficulty of the computer
     */
    public LocalGameController(GameInfo gameInfo, String playerName, Field color, int computerStrenght) {
        this.display = gameInfo.getDisplay(this);
        this.game = new Game(gameInfo, display);
        this.player = new PhysicalPlayer(playerName, color, this);
        this.opponent = new LocalMinimaxPlayer("Computer niveau " + computerStrenght, color == Field.WHITE ? Field.BLACK : Field.WHITE, this, computerStrenght);
    }

    /**
     * Constructor for a local player versus player game
     * @param gameInfo          Game information to be played
     * @param playerName        Name of the player
     * @param color             Preferred color for the player
     * @param opponentName      Name of the second player
     */
    public LocalGameController(GameInfo gameInfo, String playerName, Field color, String opponentName) {
        this.display = gameInfo.getDisplay(this);
        this.game = new Game(gameInfo, display);
        this.player = new PhysicalPlayer(playerName, color, this);
        this.opponent = new PhysicalPlayer(opponentName, color == Field.WHITE ? Field.BLACK : Field.WHITE, this);
    }

    /**
     * Thread loop.
     * Starts a new game, and manages player input. Intended for playing one game.
     * TODO: Should be able to reset and start a new game within this loop, instead of creating a new instance of this.
     */
    @Override
    public void run() {
        try {
            player.setGame(game);
            opponent.setGame(game);
        }
        catch (IllegalGamePlayerException exc) {
            exc.printStackTrace();
        }
        game.start();
        while (active) {
            if (pending) {
                pending = false;
                try { synchronized (awaitInput) { awaitInput.wait(); } }
                catch (InterruptedException exc) { exc.printStackTrace(); }
                try {
                    activePlayer.move(input);
                }
                catch (IllegalMoveException exc) {
                    System.out.println("Illegal move");
                    pending = true;
                }
            } else {
                try { synchronized (awaitGame) { awaitGame.wait(); } }
                catch (InterruptedException exc) { exc.printStackTrace(); }
            }
        }
    }

    /**
     * Called when input is expected from a player. Updates the active player.
     * @param player the player who's input is expected
     */
    @Override
    public void requestInput(Player player) {
        activePlayer = player;
        pending = true;
        synchronized (awaitGame) { awaitGame.notify(); }
    }

    /**
     * Input sent by a player or a display, to be performed by a player on the game.
     * @param move the move to perform.
     */
    @Override
    public void sendInput(int move) {
        input = move;
        synchronized (awaitInput) { awaitInput.notifyAll(); }
    }

    /**
     * Close this controller in order to end the Thread.
     */
    @Override
    public void closeController() {
        active = false;
        synchronized (awaitGame) { awaitGame.notify(); }
    }

    @Override
    public Game getGame() { return game; }

    @Override
    public Display getDisplay() { return display; }

    public String getOpponentname(){ return opponent.getName(); }
}
