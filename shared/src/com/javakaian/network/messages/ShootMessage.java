package com.javakaian.network.messages;

/**
 * @author oguz
 * <p>
 * This should be send when player wants to shoot.
 */
public class ShootMessage {

    /**
     * Player ID
     */
    private int playerId;
    /**
     * An Angle which player wants to shoot. This angle accepts player as an origin
     * point.
     */
    private float angleDeg;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public float getAngleDeg() {
        return angleDeg;
    }

    public void setAngleDeg(float angleDeg) {
        this.angleDeg = angleDeg;
    }

}
