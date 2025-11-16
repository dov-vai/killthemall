package com.javakaian.network.messages;

/**
 * Message sent from client to server to undo the last placed spike
 */
public class UndoSpikeMessage {
    private int playerId;

    public UndoSpikeMessage() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
