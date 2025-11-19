package com.javakaian.network.messages;

/**
 * Message sent from server to client when player's spike inventory is updated
 */
public class InventoryUpdateMessage {
    private int playerId;
    private int spikeCount;
    private boolean shouldReload;

    public InventoryUpdateMessage() {}

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getSpikeCount() {
        return spikeCount;
    }

    public void setSpikeCount(int spikeCount) {
        this.spikeCount = spikeCount;
    }

    public boolean isShouldReload() {
        return shouldReload;
    }
    
    public void setShouldReload(boolean shouldReload) {
        this.shouldReload = shouldReload;
    }
}
