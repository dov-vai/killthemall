package com.javakaian.shooter.teams;

/**
 * Green Team player class.
 * Communicates with other Green Team members through the ChatMediator.
 */
public class GreenTeamPlayer extends TeamPlayer {
    
    public GreenTeamPlayer(int playerId, String playerName) {
        super(playerId, playerName);
    }
    
    @Override
    public String getTeamName() {
        return "GREEN";
    }
    
    @Override
    public String getTeamColor() {
        return "#00FF00"; // Green color
    }
}
