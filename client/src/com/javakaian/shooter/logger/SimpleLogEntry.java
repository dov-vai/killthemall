package com.javakaian.shooter.logger;

public class SimpleLogEntry {
    private long timestamp;
    private String fullMessage;
    
    public SimpleLogEntry(long timestamp, String fullMessage) {
        this.timestamp = timestamp;
        this.fullMessage = fullMessage;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public String getFullMessage() {
        return fullMessage;
    }
}