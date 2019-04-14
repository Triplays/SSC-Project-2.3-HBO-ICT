package models.gamecontroller;

import models.User;
import models.display.Display;
import models.exceptions.IllegalGamePlayerException;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import models.player.PhysicalPlayer;
import models.player.Player;
import models.player.ServerMinimaxPlayer;
import models.servercom.ServerView;
import models.servercom.ServerWorker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class ServerGameController implements Runnable, GameController {

    private Player player;
    private Player opponent;
    private Game game;
    private Display display;

    // Server communication verification and game state booleans
    private boolean active = true;
    private boolean playing = false;
    private boolean serverpending = false;
    private boolean confirm = false;
    private boolean connected = false;
    private int playerinput = -1;

    private String targetServer;
    private int targetPort;

    private String name;
    private int computerStrength = -1;
    private GameInfo gameInfo;
    private ServerWorker worker;

    private HashSet<String> players = new HashSet<>();
    private HashMap<Integer, String> challenges = new HashMap<>();
    private ServerView serverView;

    // Synchronisation objects for awaiting events and input
    private final Object waitForPlayerInput = new Object();
    private final Object waitForServerConfirmation = new Object();
    private final Object waitForGameStart = new Object();

    public ServerGameController(String targetServer, int targetPort, GameInfo gameInfo, String name) {
        this.targetServer = targetServer;
        this.targetPort = targetPort;
        this.name = name;
        this.gameInfo = gameInfo;
        this.serverView = new ServerView(gameInfo, this);
        this.display = gameInfo.getDisplay(this);
    }

    public ServerGameController(String targetServer, int targetPort, GameInfo gameInfo, String name, int computerStrength) {
        this.targetServer = targetServer;
        this.targetPort = targetPort;
        this.name = name;
        this.gameInfo = gameInfo;
        this.serverView = new ServerView(gameInfo, this);
        this.computerStrength = computerStrength;
        this.display = gameInfo.getDisplay(this);
    }

    @Override
    public void run() {
        worker = new ServerWorker(targetServer, targetPort, this);
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
                    if (serverpending && playerinput >= 0) {
                        worker.sendMove(playerinput);
                        serverpending = false;
                        playerinput = -1;
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
        serverpending = true;
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

        // Assign correct player models according to the starting player
        if (myTurn) {
            player = computerStrength > 0
                    ? new ServerMinimaxPlayer(name, Field.BLACK, this, computerStrength)
                    : new PhysicalPlayer(name, Field.BLACK, this);
            opponent = new PhysicalPlayer(opponentName, Field.WHITE, this);
        } else {
            player = computerStrength > 0
                    ? new ServerMinimaxPlayer(name, Field.WHITE, this, computerStrength)
                    : new PhysicalPlayer(name, Field.WHITE, this);
            opponent = new PhysicalPlayer(opponentName, Field.BLACK, this);
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
    public void newChallenge(String gameName, String playerName, int challengeID) {
        if (computerStrength >= 0) {
            for (GameInfo gameInfo : GameInfo.values())
                if (gameInfo.gameName.equals(gameName))
                    if (gameInfo == this.gameInfo && !playing)
                        worker.acceptChallenge(challengeID);
        } else {
            for (GameInfo gameInfo : GameInfo.values())
                if (gameInfo.gameName.equals(gameName))
                    challenges.put(challengeID, playerName);
            serverView.update(players, challenges);
        }
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
        playerinput = move;
        if (serverpending) {
            synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
        }
    }

    @Override
    public void closeController() {
        active = false;
        worker.closeConnection();
        synchronized (waitForGameStart) { waitForGameStart.notifyAll(); }
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }

    public void updatePlayerList() { worker.getPlayers(); }

    public void cancelChallenge(int challengeID) {
        if (computerStrength < 0) {
            if (challenges.get(challengeID) != null) {
                challenges.remove(challengeID);
                serverView.update(players, challenges);
            }
        }
    }

    public void sendChallenge(String playerName, GameInfo gameInfo) {
        worker.sendChallenge(playerName, gameInfo);
    }

    public void acceptChallenge(int challengeID) {
        worker.acceptChallenge(challengeID);
        if (challenges.get(challengeID) != null) {
            challenges.remove(challengeID);
            serverView.update(players, challenges);
        }
    }

    public void setPlayerlist(String[] names) {
        players.clear();
        for (String name : names) if (!name.equals(User.get_username())) players.add(name);
        serverView.update(players, challenges);
    }

    public ServerView getServerView() { return serverView; }
}
