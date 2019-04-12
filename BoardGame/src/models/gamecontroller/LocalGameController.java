package models.gamecontroller;

import models.display.Display;
import models.exceptions.IllegalGamePlayerException;
import models.exceptions.IllegalMoveException;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.MinimaxPlayer;
import models.player.PhysicalPlayer;
import models.player.Player;

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
        this.player = new MinimaxPlayer("Computer niveau " + computerOneStrenght, Field.BLACK, this, computerOneStrenght);
        this.opponent = new MinimaxPlayer("Computer niveau " + computerTwoStrength, Field.WHITE, this, computerTwoStrength);
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
        this.opponent = new MinimaxPlayer("Computer niveau " + computerStrenght, color == Field.WHITE ? Field.BLACK : Field.WHITE, this, computerStrenght);
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

    @Override
    public void requestInput(Player player) {
        activePlayer = player;
        pending = true;
        synchronized (awaitGame) { awaitGame.notify(); }
    }

    @Override
    public void sendInput(int move) {
        input = move;
        synchronized (awaitInput) { awaitInput.notifyAll(); }
    }

    @Override
    public Game getGame() { return game; }

    @Override
    public Display getDisplay() { return display; }

    @Override
    public void closeController() {
        active = false;
        synchronized (awaitGame) { awaitGame.notify(); }
    }

    public String getOpponentname(){
        return opponent.getName();
    }
}
