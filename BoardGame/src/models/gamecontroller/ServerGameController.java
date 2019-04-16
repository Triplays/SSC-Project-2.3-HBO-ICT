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
    private boolean result = true;
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

    /**
     * Constructor for a player controlled server connection.
     * @param targetServer the IP to connect to.
     * @param targetPort the target port to connect to.
     * @param gameInfo the game to be played on this connection.
     * @param name the name of the player to be send to the server.
     */
    public ServerGameController(String targetServer, int targetPort, GameInfo gameInfo, String name) {
        this.targetServer = targetServer;
        this.targetPort = targetPort;
        this.name = name;
        this.gameInfo = gameInfo;
        this.serverView = new ServerView(gameInfo, this);
        this.display = gameInfo.getDisplay(this);
    }

    /**
     * Constructor for creating a computer controlled server connection.
     * @param targetServer the IP to connect to.
     * @param targetPort the target port to connect to.
     * @param gameInfo the game to be played on this connection.
     * @param name the name of the computer to be send to the server.
     * @param computerStrength the strength of the computer.
     */
    public ServerGameController(String targetServer, int targetPort, GameInfo gameInfo, String name, int computerStrength) {
        this.targetServer = targetServer;
        this.targetPort = targetPort;
        this.name = name;
        this.gameInfo = gameInfo;
        this.serverView = new ServerView(gameInfo, this);
        this.computerStrength = computerStrength;
        this.display = gameInfo.getDisplay(this);
    }

    /**
     * Thread loop.
     * Establishes connection, logs the player in, subscribes to a game, and manages input to be send to the server.
     */
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
            // Do this only when the computer is playing, as matches will start automatically
            if (computerStrength > 0) {
                while (!confirm) {
                    worker.subscribeToGame(gameInfo.gameName);
                    synchronized (waitForServerConfirmation) { waitForServerConfirmation.wait(); }
                }
            }
            confirm = false;
            updatePlayerList();
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
        this.result = result;
        confirm = true;
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
        if (result == MatchResult.DRAW) game.endGame("Niemand", comment);
        if (result == MatchResult.WIN) game.endGame(player.getName(), comment);
        if (result == MatchResult.LOSS) game.endGame(opponent.getName(), comment);

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

    public ServerView getServerView() { return serverView; }

    /**
     * Input received from the computer ot the GUI to be send to the server.
     * Ignore input of the player unless the server is requesting its input.
     * @param move the move to perform.
     */
    @Override
    public void sendInput(int move) {
        if (computerStrength > 0) {
            playerinput = move;
            if (serverpending) {
                synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
            }
        } else {
            if (serverpending) {
                playerinput = move;
                synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
            }
        }
    }

    /**
     * Notifies all synchronization objects to close off the controller, in order to end the Thread.
     */
    @Override
    public void closeController() {
        active = false;
        worker.closeConnection();
        synchronized (waitForGameStart) { waitForGameStart.notifyAll(); }
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }

    /**
     * Requests the server to update the list of players.
     */
    public void updatePlayerList() { worker.getPlayers(); }

    /**
     * Handle a cancelled challenge by the server.
     * @param challengeID
     */
    public void cancelChallenge(int challengeID) {
        if (computerStrength < 0) {
            if (challenges.get(challengeID) != null) {
                challenges.remove(challengeID);
                serverView.update(players, challenges);
            }
        }
    }

    /**
     * Send a challenge to another player on the server.
     * @param playerName the name of the player to send the challenge to.
     * @param gameInfo the game to be played.
     */
    public void sendChallenge(String playerName, GameInfo gameInfo) {
        worker.sendChallenge(playerName, gameInfo);
    }

    /**
     * Send an acceptation for a previously received challenge.
     * @param challengeID the challenge ID corresponding to the challenge.
     */
    public void acceptChallenge(int challengeID) {
        worker.acceptChallenge(challengeID);
        if (challenges.get(challengeID) != null) {
            challenges.remove(challengeID);
            serverView.update(players, challenges);
        }
    }

    /**
     * Called by the server worker when a new player list is received. Updates the view.
     * @param names the list of names of the players connected.
     */
    public void setPlayerlist(String[] names) {
        players.clear();
        for (String name : names) if (!name.equals(User.get_username())) players.add(name);
        serverView.update(players, challenges);
    }
}
