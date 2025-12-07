package com.javakaian.shooter.teams;

import com.javakaian.network.messages.ChatMessage;

/**
 * Abstract base class for team players.
 * Demonstrates Mediator pattern - each team class sends messages through the mediator.
 */
public abstract class TeamPlayer {
    
    protected int playerId;
    protected String playerName;
    
    public TeamPlayer(int playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }
    
    /**
     * Get the team name for this player.
     * Each concrete team class returns its specific team name.
     */
    public abstract String getTeamName();
    
    /**
     * Get the team color for UI rendering.
     * Each team has a distinct color.
     */
    public abstract String getTeamColor();
    
    /**
     * Called when this player receives a team chat message.
     * Can be overridden by subclasses for team-specific behavior.
     */
    public void receiveMessage(ChatMessage message) {
        // Default implementation - subclasses can override
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
