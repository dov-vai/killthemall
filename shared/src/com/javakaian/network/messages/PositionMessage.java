package com.javakaian.network.messages;

/***
 * Whenever player press a button from a keyboard this message should be send.
 */
public class PositionMessage {

    /**
     * Player ID
     */
    private int playerId;
    /**
     * The direction that player wants to move. Server will check the direction and
     * let player move if its possible.
     */
    private Direction direction;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public enum Direction {
        LEFT, RIGHT, DOWN, UP
    }

}
