package com.javakaian.shooter.mediator;

import com.javakaian.network.OServer;
import com.javakaian.network.messages.ChatMessage;
import com.javakaian.shooter.teams.TeamPlayer;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Concrete Mediator implementation for team chat.
 * Routes messages only to members of the sender's team.
 * Demonstrates Mediator pattern - team players communicate through this central mediator.
 */
public class TeamChatMediator implements ChatMediator {
    
    private final Map<Integer, TeamPlayer> teamPlayers;
    private final Map<String, List<Integer>> teamRosters;
    private final Map<Integer, Long> lastMessageTime;
    private final OServer server;
    private final Logger logger = Logger.getLogger(TeamChatMediator.class);
    
    private static final long MESSAGE_COOLDOWN_MS = 1000; // 1 second between messages
    
    public TeamChatMediator(OServer server) {
        this.server = server;
        this.teamPlayers = new HashMap<>();
        this.teamRosters = new HashMap<>();
        this.lastMessageTime = new HashMap<>();
    }
    
    @Override
    public void registerTeamPlayer(int playerId, TeamPlayer teamPlayer) {
        teamPlayers.put(playerId, teamPlayer);
        
        // Set this mediator in the team player so it can communicate
        teamPlayer.setMediator(this);
        
        String teamName = teamPlayer.getTeamName();
        teamRosters.computeIfAbsent(teamName, k -> new ArrayList<>()).add(playerId);
        
        logger.info("Player " + playerId + " (" + teamPlayer.getPlayerName() + ") joined " + teamName + " team");
        logTeamCounts();
    }
    
    @Override
    public void unregisterTeamPlayer(int playerId) {
        TeamPlayer teamPlayer = teamPlayers.remove(playerId);
        if (teamPlayer != null) {
            String teamName = teamPlayer.getTeamName();
            List<Integer> roster = teamRosters.get(teamName);
            if (roster != null) {
                roster.remove(Integer.valueOf(playerId));
                if (roster.isEmpty()) {
                    teamRosters.remove(teamName);
                }
            }
            lastMessageTime.remove(playerId);
            logger.info("Player " + playerId + " left " + teamName + " team");
            logTeamCounts();
        }
    }
    
    @Override
    public void sendMessageToTeam(TeamPlayer sender, String message) {
        if (sender == null) {
            logger.warn("Attempted to send message from null sender");
            return;
        }
        
        int senderId = sender.getPlayerId();
        
        // Verify sender is registered
        if (!teamPlayers.containsKey(senderId)) {
            logger.warn("Attempted to send message from unregistered player: " + senderId);
            return;
        }
        
        // Check rate limiting
        Long lastTime = lastMessageTime.get(senderId);
        long currentTime = System.currentTimeMillis();
        if (lastTime != null && (currentTime - lastTime) < MESSAGE_COOLDOWN_MS) {
            logger.debug("Player " + senderId + " sending messages too quickly - rate limited");
            return;
        }
        
        lastMessageTime.put(senderId, currentTime);
        
        String teamName = sender.getTeamName();
        List<Integer> teammates = teamRosters.get(teamName);
        
        if (teammates == null || teammates.isEmpty()) {
            logger.debug("No teammates found for " + teamName + " team");
            return;
        }
        
        // Create chat message
        ChatMessage chatMessage = new ChatMessage(
            senderId,
            sender.getPlayerName(),
            message,
            teamName
        );
        
        // Send to all teammates (including sender for echo)
        for (Integer teammateId : teammates) {
            TeamPlayer teammate = teamPlayers.get(teammateId);
            if (teammate != null) {
                teammate.receiveMessage(chatMessage);
                // Send message only to this specific teammate
                server.sendToUDP(teammateId, chatMessage);
            }
        }
        
        logger.debug("[" + teamName + "] Player " + senderId + " (" + sender.getPlayerName() + "): " + message);
    }
    
    /**
     * Get the number of players in a specific team.
     */
    public int getTeamSize(String teamName) {
        List<Integer> roster = teamRosters.get(teamName);
        return roster != null ? roster.size() : 0;
    }
    
    /**
     * Get the team with the fewest players (for auto-balancing).
     */
    public String getSmallestTeam() {
        String smallestTeam = "RED"; // Default
        int minSize = Integer.MAX_VALUE;
        
        for (String team : Arrays.asList("RED", "BLUE", "GREEN")) {
            int size = getTeamSize(team);
            if (size < minSize) {
                minSize = size;
                smallestTeam = team;
            }
        }
        
        return smallestTeam;
    }
    
    private void logTeamCounts() {
        logger.info("Team counts - RED: " + getTeamSize("RED") + 
                   ", BLUE: " + getTeamSize("BLUE") + 
                   ", GREEN: " + getTeamSize("GREEN"));
    }
}
