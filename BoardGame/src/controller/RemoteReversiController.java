package controller;

import game.Field;
import game.Game;
import game.ReversiGame;
import player.Player;
import servercom.ServerWorker;

import java.util.Scanner;

public class RemoteReversiController implements Runnable, Controller {

    private Player player;
    private Player opponent;
    private Game game;
    private boolean active = true;
    private boolean pending = false;

    private boolean confirm = false;

    private String name = "";


    private final Object waitForPlayerInput = new Object();
    private final Object waitForServerResponse = new Object();
    private final Object delay = new Object();

    public RemoteReversiController() {
        this.game = new ReversiGame();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {

        ServerWorker worker = new ServerWorker("localhost", 7789, this);
        Thread thread = new Thread(worker);
        thread.start();

        try {
            synchronized (delay) { delay.wait(1000); }
            Scanner scanner = new Scanner(System.in);
            while (!confirm) {
                name = scanner.nextLine();
                worker.loginPlayer(name);
                synchronized (waitForServerResponse) { waitForServerResponse.wait(); }
            }
            confirm = false;
            while (!confirm) {
                worker.subscribeToGame(game.getGameName());
                synchronized (waitForServerResponse) { waitForServerResponse.wait(); }
            }
            confirm = false;
            while (!confirm) {
                synchronized (waitForServerResponse) { waitForServerResponse.wait(); }
            }
            player.setGame(game);
            opponent.setGame(game);

            game.start();
            while (active) {
                if (pending) {
                    pending = false;
                    int move = game.giveMove(player.getColor());
                    System.out.println("SENDING MOVE: " + move);
                    worker.sendMove(move);
                } else {
                    synchronized (waitForPlayerInput) { waitForPlayerInput.wait(); }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInput(Player player) {
        pending = true;
        synchronized (waitForPlayerInput) { waitForPlayerInput.notifyAll(); }
    }

    @Override
    public void requestInput() {
        this.requestInput(player);
    }

    @Override
    public void confirmation (boolean result) {
        confirm = result;
        synchronized (waitForServerResponse) { waitForServerResponse.notifyAll(); }
    }

    @Override
    public void matchStart(String opponentName, boolean myTurn) {
        confirm = true;
        System.out.println(opponentName);
        if (myTurn) {
            player = new Player(name, Field.BLACK, this);
            opponent = new Player(opponentName, Field.WHITE, this);
        } else {
            player = new Player(name, Field.WHITE, this);
            opponent = new Player(opponentName, Field.BLACK, this);
        }
        synchronized (waitForServerResponse) { waitForServerResponse.notifyAll(); }
    }

    @Override
    public void performMove(String playerName, int target) {
        try {
            if (name.equals(playerName)) {
                game.move(player, target);
            } else {
                game.move(opponent, target);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
