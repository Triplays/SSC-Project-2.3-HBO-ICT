package player;

import helper.PositionHelper;
import game.Field;

public class UiPlayer extends Player
{

    public UiPlayer(String name)
    {
        super(name);
    }

    public Integer getMove()
    {
        // receiving user action
        // return await this.uiController.getMove();

        Integer move = this.game.giveMove(this.getColor());

        System.out.println("Move: x-" + PositionHelper.getX(move) + ", y-" + PositionHelper.getY(move));

        return move;
    }

}
