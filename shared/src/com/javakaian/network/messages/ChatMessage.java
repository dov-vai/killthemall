package com.javakaian.network.messages;

/**
 * Chat message sent between team members.
 * Demonstrates Mediator pattern - messages are routed through the ChatMediator.
 */
public class ChatMessage {
    
    private int senderId;
    private String senderName;
    private String message;
    private String teamName;
    private long timestamp;
    
    public ChatMessage() {
        // Default constructor for Kryo
    }
    
    public ChatMessage(int senderId, String senderName, String message, String teamName) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.teamName = teamName;
        this.timestamp = System.currentTimeMillis();
    }
    
    public int getSenderId() {
        return senderId;
    }
    
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
