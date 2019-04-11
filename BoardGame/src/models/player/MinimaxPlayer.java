package models.player;

import models.gamecontroller.GameController;
import models.exceptions.IllegalMoveException;
import models.game.Field;
import models.minimax.Minimax;
import models.minimax.ReversiMinimax;
import models.minimax.TicTacToeMinimax;

public class MinimaxPlayer extends Player {

    private Minimax minimax;
    private int depth;

    public MinimaxPlayer(String name, Field color, GameController gameController, int depth) {
        super(name, color, gameController);
        this.depth = depth;
        switch (gameController.getGame().getGameInfo()) {
            case REVERSI:
                minimax = new ReversiMinimax(color);
                break;
            case TICTACTOE:
                minimax = new TicTacToeMinimax(color);
                break;
        }
    }

    @Override
    public void notifyPlayer() {
        try { move(minimax.minimax(game.getBoard(), depth)); }
        catch (IllegalMoveException exc) { exc.printStackTrace(); }
    }
}
