package com.javakaian.network.messages;

/**
 * @author oguz
 * <p>
 * This message will be sent when client close or loose the game.
 */
public class LogoutMessage {

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
