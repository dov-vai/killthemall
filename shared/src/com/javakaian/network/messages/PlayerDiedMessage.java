package com.javakaian.network.messages;

/**
 * @author oguz
 * <p>
 * This message will be sent only when player died.
 */
public class PlayerDiedMessage {

    /**
     * Player id
     */
    private int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

}
