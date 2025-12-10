package com.javakaian.network.messages;

/**
 * Message confirming a player's team assignment.
 * Sent from server to all clients when a player joins a team.
 */
public class TeamAssignmentMessage {
    
    private int playerId;
    private String teamName;
    private String playerName;
    
    public TeamAssignmentMessage() {
        // Default constructor for Kryo
    }
    
    public TeamAssignmentMessage(int playerId, String teamName, String playerName) {
        this.playerId = playerId;
        this.teamName = teamName;
        this.playerName = playerName;
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
