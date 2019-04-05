package controller;

import player.Player;

public interface Controller {

    void requestInput(Player player);

    void requestInput();

    void confirmation(boolean result);

    void matchStart(String opponentName, boolean myTurn);

    void performMove(String playerName, int target);
}
