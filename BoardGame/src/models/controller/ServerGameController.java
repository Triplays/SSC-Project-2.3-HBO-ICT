package models.controller;

import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.exceptions.IllegalGamePlayerException;
import models.game.Field;
import models.game.Game;
import models.game.GameInfo;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import models.player.Player;
import models.servercom.ServerWorker;

import java.util.Scanner;

public class ServerGameController implements Runnable, GameController, ServerController {

    private Player player;
    private Player opponent;
    private Game game;
    private Pane pane;

    private boolean active = true;
    private boolean playing = false;
    private boolean pending = false;

    private boolean confirm = false;

    private String name = "";

    private final Object waitForPlayerInput = new Object();
    private final Object waitForServerConfirmation = new Object();
    private final Object waitForGameStart = new Object();

    private Minimax minimax;

    public ServerGameController() {
        pane = new Pane();
    }

    public Pane getPane() {
        return pane;
    }

    @Override
    public void run() {

        ServerWorker worker = new ServerWorker("145.37.172.171", 7789, this);
        Thread thread = new Thread(worker);
        thread.start();

        try {
            Scanner scanner = new Scanner(System.in);
            while (!confirm) {
                // TODO: Obtain input on what name to use
                name = scanner.nextLine();
                worker.loginPlayer(name);
                synchronized (waitForServerConfirmation) { waitForServerConfirmation.wait(); }
            }
            confirm = false;
            while (!confirm) {
                // TODO: Obtain input on what game to play
                worker.subscribeToGame(GameInfo.REVERSI.gameName);
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

    @Override
    public void requestInput() {
        pending = true;
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
    }

    @Override
    public void confirmation (boolean result) {
        confirm = result;
        synchronized (waitForServerConfirmation) { waitForServerConfirmation.notifyAll(); }
    }

    @Override
    public void matchStart(String opponentName, boolean myTurn, String gameName) {
        playing = true;
        for (GameInfo gameInfo : GameInfo.values()) {
            if (gameInfo.gameName.equals(gameName)) {
                game = new Game(gameInfo);
                break;
            }
        }

        if (myTurn) {
            player = new Player(name, Field.BLACK, this);
            opponent = new Player(opponentName, Field.WHITE, this);
            minimax = new ReversiMinimax(Field.BLACK);
        } else {
            player = new Player(name, Field.WHITE, this);
            opponent = new Player(opponentName, Field.BLACK, this);
            minimax = new ReversiMinimax(Field.WHITE);
        }
        synchronized (waitForGameStart) { waitForGameStart.notifyAll(); }
    }

    @Override
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

    @Override
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
}
