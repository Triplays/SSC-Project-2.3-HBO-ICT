package models.game;

/**
 * States of the game after a move has been made.
 * WINWHITE, WINBLACK, and DRAW mean that the game has ended with the given result.
 * SWAP and STAY describe what action to take after this move.
 */
public enum Gamestate {
    SWAP, STAY, WINWHITE, WINBLACK, DRAW
}
