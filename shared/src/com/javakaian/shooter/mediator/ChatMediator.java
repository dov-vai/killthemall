package com.javakaian.shooter.mediator;

import com.javakaian.shooter.teams.TeamPlayer;

/**
 * Mediator interface for team chat communication.
 * Demonstrates Mediator pattern - centralizes communication between team members.
 */
public interface ChatMediator {
    
    /**
     * Register a team player with the mediator.
     * @param playerId The player's unique ID
     * @param teamPlayer The team player instance
     */
    void registerTeamPlayer(int playerId, TeamPlayer teamPlayer);
    
    /**
     * Unregister a team player from the mediator.
     * @param playerId The player's unique ID
     */
    void unregisterTeamPlayer(int playerId);
    
    /**
     * Send a message to all members of the sender's team.
     * @param sender The team player sending the message
     * @param message The message content
     */
    void sendMessageToTeam(TeamPlayer sender, String message);
}
