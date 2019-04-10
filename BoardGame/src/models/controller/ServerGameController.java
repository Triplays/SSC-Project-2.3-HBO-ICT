package models.controller;

import models.exceptions.IllegalGamePlayerException;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import models.minimax.Minimax;
import models.player.PhysicalPlayer;
import models.player.Player;
import models.servercom.ServerWorker;

public class ServerGameController implements Runnable, GameController {

    private Player player;
    private Player opponent;
    private Game game;
    private Pane pane = new Pane();

    private boolean active = true;
    private boolean playing = false;
    private boolean pending = false;
    private boolean confirm = false;

    private String name;
    private int computerStrength = 0;
    private GameInfo gameInfo;
    private Minimax minimax;

    private final Object waitForPlayerInput = new Object();
    private final Object waitForServerConfirmation = new Object();
    private final Object waitForGameStart = new Object();

    public ServerGameController(GameInfo gameInfo, String name) {
        this.name = name;
        this.gameInfo = gameInfo;
    }

    public ServerGameController(GameInfo gameInfo, String name, int computerStrength) {
        this.name = name;
        this.gameInfo = gameInfo;
        this.computerStrength = computerStrength;
    }

    @Override
    public Pane getPane() { return pane; }

    @Override
    public Game getGame() { return game; }

    @Override
    public void run() {

        ServerWorker worker = new ServerWorker("145.33.225.170", 7789, this);
        Thread thread = new Thread(worker);
        thread.start();

        try {
            while (!confirm) {
                worker.loginPlayer(name);
                synchronized (waitForServerConfirmation) { waitForServerConfirmation.wait(); }
            }
            confirm = false;
            while (!confirm) {
                worker.subscribeToGame(gameInfo.gameName);
                synchronized (waitForServerConfirmation) { waitForServerConfirmation.wait(); }
            }
            confirm = false;

            while (active) {
                while (!playing) {
                    synchronized (waitForGameStart) { waitForGameStart.wait(); }
                }
                startGame();
                while (playing) {
                    if (pending) {
                        pending = false;
                        worker.sendMove(minimax.minimax(game.getBoard(), 4));
                    } else {
                        synchronized (waitForPlayerInput) { waitForPlayerInput.wait(); }
                    }
                }
            }
        }
        catch (InterruptedException exc) { exc.printStackTrace(); }
        catch (IllegalGamePlayerException exc) { exc.printStackTrace(); }
    }

    private void startGame() throws IllegalGamePlayerException {
        Platform.runLater(() -> {
            pane.getChildren().clear();
            pane.getChildren().add(game.getDisplay().getWrapperPane());
        });
        player.setGame(game);
        opponent.setGame(game);
        game.start();
    }

    @Override
    public void requestInput(Player player) {}

    public void requestInput() {
        pending = true;
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
    }

    public void confirmation (boolean result) {
        confirm = result;
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }

    public void matchStart(String opponentName, boolean myTurn, String gameName) {
        playing = true;
        for (GameInfo gameInfo : GameInfo.values()) {
            if (gameInfo.gameName.equals(gameName)) {
                game = new Game(gameInfo);
                break;
            }
        }

        if (myTurn) {
            player = new PhysicalPlayer(name, Field.BLACK, this);
            opponent = new PhysicalPlayer(opponentName, Field.WHITE, this);
        } else {
            player = new PhysicalPlayer(name, Field.WHITE, this);
            opponent = new PhysicalPlayer(opponentName, Field.BLACK, this);
        }
        synchronized (waitForGameStart) { waitForGameStart.notifyAll(); }
    }

    public void performMove(String playerName, int target, String details) {
        // TODO: Something useful with details?
        if (details.length() > 0 ) System.out.println("Details: " + details);
        try {
            if (name.equals(playerName)) {
                player.move(target);
            } else {
                opponent.move(target);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void matchEnd(MatchResult result, int scoreOne, int scoreTwo, String comment) {
        playing = false;

        System.out.println("Result: " + result.toString());
        System.out.println("Player one: " + scoreOne);
        System.out.println("Player two: " + scoreTwo);
        System.out.println("Comment: " + comment);

        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
    }

    public void closeController() {
        active = false;
        synchronized (waitForGameStart) { waitForGameStart.notifyAll(); }
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }

    public boolean acceptChallenge() {
        return !playing;
    }
}
