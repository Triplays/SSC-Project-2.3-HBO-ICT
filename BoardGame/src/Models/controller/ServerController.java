package controller;

public interface ServerController {
    void requestInput();

    void confirmation(boolean result);

    void matchStart(String opponentName, boolean myTurn, String gameName);

    void performMove(String playerName, int target, String details);

    void matchEnd(MatchResult result, int scoreOne, int scoreTwo, String comment);
}
