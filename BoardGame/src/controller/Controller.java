package controller;

import player.Player;

public interface Controller {

    void requestInput(Player player);

    void confirmation (boolean result);

    void matchStart (String opponentName, boolean myTurn);
}
