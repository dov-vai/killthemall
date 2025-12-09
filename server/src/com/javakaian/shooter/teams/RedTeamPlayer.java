package com.javakaian.shooter.teams;

/**
 * Red Team player class.
 * Communicates with other Red Team members through the ChatMediator.
 */
public class RedTeamPlayer extends TeamPlayer {
    
    public RedTeamPlayer(int playerId, String playerName) {
        super(playerId, playerName);
    }
    
    @Override
    public String getTeamName() {
        return "RED";
    }
    
    @Override
    public String getTeamColor() {
        return "#FF0000"; // Red color
    }
}
