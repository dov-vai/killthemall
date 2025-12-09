package com.javakaian.shooter.teams;

/**
 * Blue Team player class.
 * Communicates with other Blue Team members through the ChatMediator.
 */
public class BlueTeamPlayer extends TeamPlayer {
    
    public BlueTeamPlayer(int playerId, String playerName) {
        super(playerId, playerName);
    }
    
    @Override
    public String getTeamName() {
        return "BLUE";
    }
    
    @Override
    public String getTeamColor() {
        return "#0000FF"; // Blue color
    }
}
