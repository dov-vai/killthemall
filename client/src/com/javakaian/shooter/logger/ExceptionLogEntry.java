package com.javakaian.shooter.logger;

public class ExceptionLogEntry {
    private long timestamp;
    private String fullMessage;
    private String exceptionMessage;
    
    public ExceptionLogEntry(long timestamp, String fullMessage, String exceptionMessage) {
        this.timestamp = timestamp;
        this.fullMessage = fullMessage;
        this.exceptionMessage = exceptionMessage;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public String getFullMessage() {
        return fullMessage;
    }
    
    public String getExceptionMessage() {
        return exceptionMessage;
    }
}