package controller;

import exceptions.IllegalGamePlayerException;
import game.Game;
import player.Player;

public interface Controller {

    void requestInput(Player player);

    void confirmation (boolean result);

    void matchStart (String opponentName, boolean myTurn) throws IllegalGamePlayerException;

    Game getGame();
}
