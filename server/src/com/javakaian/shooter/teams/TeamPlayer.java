package com.javakaian.shooter.teams;

import com.javakaian.network.messages.ChatMessage;
import com.javakaian.shooter.mediator.ChatMediator;

/**
 * Abstract base class for team players.
 * Demonstrates Mediator pattern - each team member holds a reference to the mediator
 * and communicates with teammates through it.
 */
public abstract class TeamPlayer {
    
    protected int playerId;
    protected String playerName;
    protected ChatMediator mediator;
    
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
     * Set the mediator for this team player.
     * Called when the player registers with the mediator.
     */
    public void setMediator(ChatMediator mediator) {
        this.mediator = mediator;
    }
    
    /**
     * Send a message to all team members through the mediator.
     * This is the primary way team members communicate with each other.
     */
    public void sendMessage(String message) {
        if (mediator != null) {
            mediator.sendMessageToTeam(this, message);
        }
    }
    
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
