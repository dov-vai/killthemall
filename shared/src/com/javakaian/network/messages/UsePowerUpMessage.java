package com.javakaian.network.messages;

/**
 * Message sent from client to server when player wants to use a stored power-up
 */
public class UsePowerUpMessage {
    private int playerId;
    private int powerUpSlot; // 0-3 for slots mapped to keys F1-F4

    public UsePowerUpMessage() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPowerUpSlot() {
        return powerUpSlot;
    }

    public void setPowerUpSlot(int powerUpSlot) {
        this.powerUpSlot = powerUpSlot;
    }
}
