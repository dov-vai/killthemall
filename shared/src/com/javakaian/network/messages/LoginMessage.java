package com.javakaian.network.messages;

/**
 * @author oguz
 * <p>
 * This message will only be sent by clients when they want to join the
 * game.
 */
public class LoginMessage {

    /**
     * Player id
     */
    private int playerId;
    /**
     * Players current X,Y coordinates
     */
    private float x;
    private float y;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
