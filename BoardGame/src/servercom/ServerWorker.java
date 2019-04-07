package servercom;

import controller.MatchResult;
import controller.ServerController;

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
    private ServerController controller;

    public ServerWorker(String address, int port, ServerController controller) {
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
                String[] lines = new String((Arrays.copyOfRange(bytes, 0, count))).split("\n");
                for (String line : lines) if (line.length() > 0) handleResponse(line.split(" ", 4));
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
            controller.confirmation(true);
        } else if (response[0].startsWith("ERR")) {
            // TODO: Handle error response
            System.out.println("Error");
            System.out.println(Arrays.toString(response));
            controller.confirmation(false);
        } else if (response[0].startsWith("SVR")) {
            if (response[1].equals("GAME")) {
                handleGameResponse(response);
            } else {
                // TODO: Handle unknown response (help should not be called here)
                System.out.println("Unknown SVR {} argument: " + response[1]);
            }
        } else {
            // TODO: Handle unknown response
            System.out.println("Unknown first argument: " + response[0]);
        }
    }

    private void handleGameResponse(String[] response) {
        switch (response[2]) {
            case "WIN":
                handleMatchResult(MatchResult.WIN, response[3]);
                break;
            case "LOSS":
                handleMatchResult(MatchResult.LOSS, response[3]);
                break;
            case "DRAW":
                handleMatchResult(MatchResult.DRAW, response[3]);
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
                System.out.println("Unknown SVR GAME {} argument: " + response[2]);
                break;
        }
    }

    private void handleMatchResult(MatchResult result, String response) {
        System.out.println(response);

        String[] arguments = response.substring(response.indexOf("{") + 1, response.lastIndexOf("}")).split(", ");
        int scoreOne = 0;
        int scoreTwo = 0;
        String comment = "";

        for (String arg : arguments) {
            if (arg.startsWith("PLAYERONESCORE")) {
                int index = arg.indexOf("\"", arg.indexOf("PLAYERONESCORE") + 14) + 1;
                scoreOne = parseInt(arg.substring(index, arg.indexOf("\"", index)));
            } else if (arg.startsWith("PLAYERTWOSCORE")) {
                int index = arg.indexOf("\"", arg.indexOf("PLAYERTWOSCORE") + 14) + 1;
                scoreTwo = parseInt(arg.substring(index, arg.indexOf("\"", index)));
            } else if (arg.startsWith("COMMENT")) {
                int index = arg.indexOf("\"", arg.indexOf("COMMENT") + 7) + 1;
                comment = arg.substring(index, arg.indexOf("\"", index));
            } else {
                // TODO: Handle unknown argument
                System.out.println("match end err: " + arg);
            }
        }
        controller.matchEnd(result, scoreOne, scoreTwo, comment);
    }

    private void handleYourTurn(String response) {
        controller.requestInput();
    }

    private void handleMove(String response) {
        String[] arguments = response.substring(response.indexOf("{") + 1, response.lastIndexOf("}")).split(", ");
        String playerName = "";
        String details = "";
        int target = 0;

        for (String arg : arguments) {
            if (arg.startsWith("PLAYER")) {
                int index = arg.indexOf("\"", arg.indexOf("PLAYER") + 6) + 1;
                playerName = arg.substring(index, arg.indexOf("\"", index));
            } else if (arg.startsWith("MOVE")) {
                int index = arg.indexOf("\"", arg.indexOf("MOVE") + 4) + 1;
                target = parseInt(arg.substring(index, arg.indexOf("\"", index)));
            } else if (arg.startsWith("DETAILS")) {
                int index = arg.indexOf("\"", arg.indexOf("DETAILS") + 6) + 1;
                details = arg.substring(index, arg.indexOf("\"", index));
            } else {
                // TODO: Handle unknown argument
                System.out.println("move err: " + arg);
            }
        }

        controller.performMove(playerName, target, details);
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
                playerToMove = arg.substring(index + 1, arg.indexOf("\"", index + 1));
            } else if (arg.startsWith("OPPONENT")) {
                int index = arg.indexOf("\"", arg.indexOf("OPPONENT") + 8);
                opponentName = arg.substring(index + 1, arg.indexOf("\"", index + 1));
            } else if (arg.startsWith("GAMETYPE")) {
                int index = arg.indexOf("\"", arg.indexOf("GAMETYPE") + 8);
                gameName = arg.substring(index + 1, arg.indexOf("\"", index + 1));
            }
        }
        myTurn = !playerToMove.equals(opponentName);
        controller.matchStart(opponentName, myTurn, gameName);
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
