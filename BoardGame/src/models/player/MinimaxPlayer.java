package models.player;

import models.exception.UnknownGameException;
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
        super(name);

        try {
            this.setController(gameController);
            this.setColor(color);
        } catch (UnknownGameException e) {
            System.out.println("Unable to load player.");
        }

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
