package models.gamecontroller;

import models.config.ReversiIndicatorSet;
import models.display.Display;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.exceptions.IllegalGamePlayerException;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.MinimaxPlayer;
import models.player.PhysicalPlayer;
import models.player.Player;
import models.servercom.ServerWorker;

public class ServerGameController implements Runnable, GameController {

    private Player player;
    private Player opponent;
    private Game game;
    private Display display;

    // Server communication verification and game state booleans
    private boolean active = true;
    private boolean playing = false;
    private boolean pending = false;
    private boolean confirm = false;
    private boolean connected = false;

    private String name;
    private int computerStrength = 0;
    private GameInfo gameInfo;
    private Minimax minimax;

    // Synchronisation objects for awaiting events and input
    private final Object waitForPlayerInput = new Object();
    private final Object waitForServerConfirmation = new Object();
    private final Object waitForGameStart = new Object();

    public ServerGameController(GameInfo gameInfo, String name) {
        this.name = name;
        this.gameInfo = gameInfo;
        this.display = gameInfo.getDisplay(this);
    }

    public ServerGameController(GameInfo gameInfo, String name, int computerStrength) {
        this.name = name;
        this.gameInfo = gameInfo;
        this.computerStrength = computerStrength;
        this.display = gameInfo.getDisplay(this);
    }

    @Override
    public void run() {
        ServerWorker worker = new ServerWorker("145.37.148.200", 7789, this);
        Thread thread = new Thread(worker);
        thread.start();

        try {
            // Wait for the connection to the server to be established
            while (!connected)
                synchronized (waitForServerConfirmation) { waitForServerConfirmation.wait(); }
            // Login the player to the server, and wait for confirmation
            while (!confirm) {
                worker.loginPlayer(name);
                synchronized (waitForServerConfirmation) { waitForServerConfirmation.wait(); }
            }
            confirm = false;
            // Subscribe to the game of preference, given in the constructor
            while (!confirm) {
                worker.subscribeToGame(gameInfo.gameName);
                synchronized (waitForServerConfirmation) { waitForServerConfirmation.wait(); }
            }
            confirm = false;

            // Active loop for acquiring input and restarting games
            while (active) {
                while (!playing) {
                    synchronized (waitForGameStart) { waitForGameStart.wait(); }
                }
                startGame();
                while (playing) {
                    if (pending) {
                        pending = false;
                        worker.sendMove(minimax.minimax(game.getBoard(), computerStrength));
                    } else {
                        synchronized (waitForPlayerInput) { waitForPlayerInput.wait(); }
                    }
                }
            }
        }
        catch (InterruptedException exc) { exc.printStackTrace(); }
        catch (IllegalGamePlayerException exc) { exc.printStackTrace(); }
    }

    /**
     * Start the local game for verification and view purposes
     * @throws IllegalGamePlayerException if there were already two players, or if a duplicate color gets registered
     */
    private void startGame() throws IllegalGamePlayerException {
        player.setGame(game);
        opponent.setGame(game);
        game.start();
    }

    /**
     * The local Game is being updated based on server verification. Ignore requests for input from local game.
     * @param player Player whoms input is required
     */
    @Override
    public void requestInput(Player player) {}

    /**
     * Input request made by the server. Continue thread loop to acquire input
     */
    public void requestInput() {
        pending = true;
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
    }

    /**
     * Confirmation result of the last sent message
     * @param result whether the confirmation was valid
     */
    public void confirmation (boolean result) {
        confirm = result;
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }

    /**
     * Start of a match indicated by the server. Assign correct roles.
     * @param opponentName  Name of the opponent
     * @param myTurn        Whether the local player starts
     * @param gameName      Name of the game to be played
     */
    public void matchStart(String opponentName, boolean myTurn, String gameName) {
        playing = true;

        // Lookup the correct GameInfo and start the game
        for (GameInfo gameInfo : GameInfo.values()) {
            if (gameInfo.gameName.equals(gameName)) {
                game = new Game(gameInfo, display);
                break;
            }
        }

        ReversiIndicatorSet reversiIndicatorSet = new ReversiIndicatorSet(8);
        reversiIndicatorSet.setLineIndicator(2.7);

        // Assign correct player models according to the starting player
        if (myTurn) {
            //player = new PhysicalPlayer(name, Field.BLACK, this);
            player = new PhysicalPlayer(name, Field.BLACK, this);
            opponent = new PhysicalPlayer(opponentName, Field.WHITE, this);
            minimax = new ReversiMinimax(Field.BLACK, reversiIndicatorSet);
        } else {
            //player = new PhysicalPlayer(name, Field.WHITE, this);
            player = new PhysicalPlayer(name, Field.WHITE, this);
            opponent = new PhysicalPlayer(opponentName, Field.BLACK, this);
            minimax = new ReversiMinimax(Field.WHITE, reversiIndicatorSet);
        }

        synchronized (waitForGameStart) { waitForGameStart.notifyAll(); }
    }

    /**
     * Perform a move as described by the server
     * @param playerName    Player to perform the move
     * @param target        The Field that has been played
     * @param details       Eventual details of the move. Only known response is "Illegal move"
     */
    public void performMove(String playerName, int target, String details) {
        if (details.startsWith("Illegal move")) return;
        try {
            if (name.equals(playerName)) {
                player.move(target);
            } else {
                opponent.move(target);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Server confirms that the match has ended
     * @param result    result whether the client has won, lost, or drawn
     * @param scoreOne  score of player one
     * @param scoreTwo  score of player two
     * @param comment   eventual comments
     */
    public void matchEnd(MatchResult result, int scoreOne, int scoreTwo, String comment) {
        playing = false;

        System.out.println("Result: " + result.toString());
        System.out.println("Player one: " + scoreOne);
        System.out.println("Player two: " + scoreTwo);
        System.out.println("Comment: " + comment);

        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
    }

    /**
     * @return whether the client is busy right now
     */
    public boolean isAvaiable() { return !playing; }

    /**
     * Server requests whether the client wants to accept a challenge for a game.
     * Accept if the client is not playing and subscribed to said game.
     * @param gameName the game that is requested
     * @return whether the client accepts the challenge
     */
    public boolean acceptChallenge(String gameName) {
        for (GameInfo gameInfo : GameInfo.values())
            if (gameInfo.gameName.equals(gameName))
                if (gameInfo == this.gameInfo && !playing)
                    return true;
        return false;
    }

    /**
     * Server confirmation that the connection is established
     */
    public void connectionSuccesful(){
        connected = true;
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }

    @Override
    public Display getDisplay() { return display; }

    @Override
    public Game getGame() { return game; }

    @Override
    public void sendInput(int move) {

    }

    @Override
    public void closeController() {
        active = false;
        synchronized (waitForGameStart) { waitForGameStart.notifyAll(); }
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }
}
