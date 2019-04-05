package servercom;

import controller.Controller;
import game.Field;
import player.Player;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class ServerWorker implements Runnable {

    private String address ;
    private int port;
    private InputStream in;
    private OutputStream out;

    private byte[] bytes = new byte[3200];
    private int count;
    private Controller controller;

    public ServerWorker(String address, int port, Controller controller) {
        this.address = address;
        this.port = port;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            Socket connection = new Socket(address, port);
            in = connection.getInputStream();
            out = connection.getOutputStream();

            while ((count = in.read(bytes)) > 0) {
                handleResponse(new String((Arrays.copyOfRange(bytes, 0, count))).split(" ", 4));
            }
            System.out.println("Stream has closed");
            in.close();
            out.close();
            connection.close();
        }
        catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void handleResponse(String[] response) {
        if (response[0].startsWith("OK")) {
            System.out.println("Confirmed");
            controller.confirmation(true);
        }
        if (response[0].startsWith("ERR")) {
            System.out.println("Denied");
            controller.confirmation(false);
        }
        if (response[0].startsWith("SVR")) {
            if (response[1].equals("GAME")) {
                handleGameResponse(response);
            } else {
                // TODO: Handle unknown response (help should not be called here)
                System.out.println("Send help");
            }
        }
    }

    private void handleGameResponse(String[] response) {
        switch (response[2]) {
            case "WIN":
                break;
            case "LOSS":
                break;
            case "DRAW":
                break;
            case "MATCH":
                handleMatchStart(response[3]);
                break;
            case "MOVE":
                handleMove((response[3]));
                break;
            case "YOURTURN":
                handleYourTurn(response[3]);
                break;
            default:
                // TODO: Handle unknown response
                System.out.println("Panic");
                break;
        }
        System.out.println(response[3]);
    }

    private void handleHelpResponse(String[] response) {
        switch (response[2]) {

        }
        System.out.println("Panic Help");
    }

    private void handleYourTurn(String response) {
        controller.requestInput();
    }

    private void handleMove(String response) {
        String[] arguments = response.substring(response.indexOf("{") + 1, response.lastIndexOf("}")).split(", ");
        String playerName = "";
        int target = 0;

        for (String arg : arguments) {
            if (arg.startsWith("PLAYER")) {
                int index = arg.indexOf("\"", arg.indexOf("PLAYER") + 6);
                playerName = arg.substring(index + 1, arg.indexOf("\"", index + 2));
            } else if (arg.startsWith("MOVE")) {
                int index = arg.indexOf("\"", arg.indexOf("MOVE") + 4);
                target = parseInt(arg.substring(index + 1, arg.indexOf("\"", index + 2)));
            } else {
                // TODO: Handle the unknown
            }
        }

        controller.performMove(playerName, target);
    }

    private void handleMatchStart(String response) {
        String[] arguments = response.substring(response.indexOf("{") + 1, response.lastIndexOf("}")).split(", ");
        String opponentName = "";
        String playerToMove = "";
        String gameName = "";
        boolean myTurn;

        for (String arg : arguments) {
            if (arg.startsWith("PLAYERTOMOVE")) {
                int index = arg.indexOf("\"", arg.indexOf("PLAYERTOMOVE") + 12);
                playerToMove = arg.substring(index + 1, arg.indexOf("\"", index + 2));
            } else if (arg.startsWith("OPPONENT")) {
                int index = arg.indexOf("\"", arg.indexOf("OPPONENT") + 8);
                opponentName = arg.substring(index + 1, arg.indexOf("\"", index + 2));
            } else if (arg.startsWith("GAMETYPE")) {
                int index = arg.indexOf("\"", arg.indexOf("GAMETYPE") + 8);
                gameName = arg.substring(index + 1, arg.indexOf("\"", index + 2));
            } else {
                // TODO: Handle the unknown
            }
        }
        System.out.println("p2m: " + playerToMove + ", on: " + opponentName);
        myTurn = !playerToMove.equals(opponentName);
        controller.matchStart(opponentName, myTurn);
    }

    public void sendMessage(String s) {
        try {
            out.write(s.getBytes());
            out.write("\r\n".getBytes());
            out.flush();
        }
        catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void loginPlayer(String playerName) {
        sendMessage("login " + playerName);

    }

    public void subscribeToGame(String gameName) {
        sendMessage("subscribe " + gameName);
    }

    public void sendMove(int pos) {
        sendMessage("move " + pos);
    }
}
