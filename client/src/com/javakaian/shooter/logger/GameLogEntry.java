package com.javakaian.shooter.logger;

public class GameLogEntry {
    private long timestamp;
    private String eventType;
    private String message;
    private String severity;

    public GameLogEntry(long timestamp, String eventType, String message, String severity) {
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.message = message;
        this.severity = severity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }
}