package models.servercom;

import models.game.GameInfo;
import models.gamecontroller.MatchResult;
import models.gamecontroller.ServerGameController;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

/**
 * Handles connectivity to the server.
 * Reads input stream and sends processed data to the bound controller.
 * Sends messages over the output stream on request from the controller.
 */
public class ServerWorker implements Runnable {

    private String address;
    private int port;
    private Socket connection;
    private InputStream in;
    private OutputStream out;

    private byte[] bytes = new byte[3200];
    private ServerGameController controller;

    /**
     * The constructor sets the communication information
     * @param address the IP address to connect to
     * @param port the target port to connect to
     * @param controller the controller to pass the results to after processing
     */
    public ServerWorker(String address, int port, ServerGameController controller) {
        this.address = address;
        this.port = port;
        this.controller = controller;
    }

    /**
     * Thread loop.
     * Establishes the connection, and delegates the input to the handlers once received.
     */
    @Override
    public void run() {
        try {
            connection = new Socket(address, port);
            in = connection.getInputStream();
            out = connection.getOutputStream();
            controller.connectionSuccesful();
            int count;
            while ((count = in.read(bytes)) > 0) {
                String[] lines = new String((Arrays.copyOfRange(bytes, 0, count))).split("\n");
                for (String line : lines) if (line.length() > 0) handleResponse(line.split(" ", 4));
            }
        }
        catch (java.io.IOException exc) {
            exc.printStackTrace();
        }
        finally {
            closeConnection();
            System.out.println("Stream has closed");
        }
    }

    /**
     * Handles the initial processing of the response, and delegates the processing
     * @param response the response from the server, separated on spaces into maximal 4 chunks
     */
    private void handleResponse(String[] response) {
        if (response[0].startsWith("OK")) {
            controller.confirmation(true);
        } else if (response[0].startsWith("ERR")) {
            System.out.println("ERR: ");
            controller.confirmation(false);
        } else if (response[0].startsWith("SVR")) {
            if (response[1].equals("GAME")) {
                handleGameResponse(response);
            } else if (response[1].startsWith("PLAYERLIST")) {
                handlePlayerList(response);
            } else {
                // TODO: Handle unknown response (help should not be called here)
            }
        } else {
            // TODO: Handle unknown response
            System.out.println("Unknown first argument: " + response[0]);
        }
    }

    /**
     * Processes the list of players received.
     * Stitches the received response back together as it splits the first name off the others
     * @param response the response from the server, separated on spaces into maximal 4 chunks
     */
    private void handlePlayerList(String[] response) {
        String stitch = String.join(" ", response);
        String[] names =
                stitch.substring(stitch.indexOf("[") + 1, stitch.lastIndexOf("]")).replaceAll("\"", "").split(", ");
        for (String name : names) name = name.substring(1, name.length() - 1);
        controller.setPlayerlist(names);
    }

    /**
     * Handles the response if it is a server game response. Delegates the processing based on the third argument
     * @param response the response from the server, separated on spaces into maximal 4 chunks
     */
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
            case "CHALLENGE":
                handleChallenge(response[3]);
                break;
            default:
                // TODO: Handle unknown response
                System.out.println("Unknown SVR GAME {} argument: " + response[2]);
                break;
        }
    }

    /**
     * Handles the processing of challenge invites and cancellations
     * Updates these challenges on the controller.
     * @param response the server response stripped from its prefixes
     */
    private void handleChallenge(String response) {
        if (response.startsWith("CANCELLED")) {
            int index = response.indexOf("\"", response.indexOf("CHALLENGENUMBER") + 15) + 1;
            int challengeID = parseInt(response.substring(index, response.indexOf("\"", index)));
            controller.cancelChallenge(challengeID);
        }
        else if (controller.isAvaiable()) {
            String[] arguments = response.substring(response.indexOf("{") + 1, response.lastIndexOf("}")).split(", ");
            String challenger = "";
            String gametype = "";
            int challengeID = 0;

            for (String arg : arguments) {
                if (arg.startsWith("CHALLENGER")) {
                    int index = arg.indexOf("\"", arg.indexOf("CHALLENGER") + 10) + 1;
                    challenger = arg.substring(index, arg.indexOf("\"", index));
                } else if (arg.startsWith("GAMETYPE")) {
                    int index = arg.indexOf("\"", arg.indexOf("GAMETYPE") + 8) + 1;
                    gametype = arg.substring(index, arg.indexOf("\"", index));
                } else if (arg.startsWith("CHALLENGENUMBER")) {
                    int index = arg.indexOf("\"", arg.indexOf("CHALLENGENUMBER") + 15) + 1;
                    challengeID = parseInt(arg.substring(index, arg.indexOf("\"", index)));
                } else {
                    // TODO: Handle unknown argument
                    System.out.println("match end err: " + arg);
                }
            }
            controller.newChallenge(gametype, challenger, challengeID);

        }
    }

    /**
     * Handles the end of a game by sending it to the controller. Result is determined in the prefix
     * @param result the result of the game as of given in the prefix
     * @param response the server response stripped from its prefixes
     */
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

    /**
     * Handles a turn notification. Controller will be notified to obtain input.
     * Response could be removed for now, as the details won't matter as its destined for this end user anyway.
     * @param response the server response stripped from its prefixes.
     */
    private void handleYourTurn(String response) {
        controller.requestInput();
    }

    /**
     * Handles a confirmed move as sent by the server. Can be either the client or the opponent that moved.
     * Sends the move data to the controller
     * @param response the server response stripped from its prefixes.
     */
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

    /**
     * Handles the start of a new match. Assigns opponent, starting player, and gametype.
     * @param response the server response stripped from its prefixes.
     */
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

    /**
     * Send a message to the server. Usually called from the controller or through helper functions below.
     * @param s the message to be sent.
     */
    private void sendMessage(String s) {
        try {
            out.write(s.getBytes());
            out.write("\r\n".getBytes());
            out.flush();
        }
        catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Update the playerlist. The controller will be notified with the result.
     */
    public void getPlayers() {
        sendMessage("get playerlist");
    }

    /**
     * Login a player to the server.
     * @param playerName name of the player
     */
    public void loginPlayer(String playerName) {
        sendMessage("login " + playerName);
    }

    /**
     * Subscribe to a game type. This will automatically start a game when an opponent subscribes as well.
     * @param gameName name of the game to subscribe to.
     */
    public void subscribeToGame(String gameName) {
        sendMessage("subscribe " + gameName);
    }

    /**
     * Send a move to the server.
     * @param pos the position to move to.
     */
    public void sendMove(int pos) {
        sendMessage("move " + pos);
    }

    /**
     * Send a challenge to a player.
     * @param playerName the player to be challenged.
     * @param gameInfo the game to be played.
     */
    public void sendChallenge(String playerName, GameInfo gameInfo) {
        sendMessage("challenge \"" + playerName + "\" \"" + gameInfo.gameName + "\"");
    }

    /**
     * Accept a previously received challenge.
     * @param challengeID the challenge ID that was attached to the challenge invite.
     */
    public void acceptChallenge(int challengeID) {
        sendMessage("challenge accept " + challengeID);
    }

    /**
     * Close input and output streams, and the socket.
     */
    public void closeConnection() {
        try {
            in.close();
            out.close();
            connection.close();
        }
        // TODO: Something when this throws an exception? It is expected if the connection was refused.
        catch (IOException exc) { exc.printStackTrace(); }
        catch (NullPointerException exc) { exc.printStackTrace(); }
    }
}
