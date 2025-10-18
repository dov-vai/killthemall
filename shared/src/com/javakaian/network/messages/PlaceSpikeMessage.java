package com.javakaian.network.messages;

/**
 * Message sent from client to server when player wants to place a spike
 */
public class PlaceSpikeMessage {
    private int playerId;
    private float rotation; // rotation angle in degrees (from aimline)

    public PlaceSpikeMessage() {}

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
