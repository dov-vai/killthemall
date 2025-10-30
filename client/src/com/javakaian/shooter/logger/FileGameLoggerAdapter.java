package com.javakaian.shooter.logger;

public class FileGameLoggerAdapter implements IGameLogger {
    
    private FileGameLogger fileLogger;
    
    public FileGameLoggerAdapter(String filePath) {
        this.fileLogger = new FileGameLogger(filePath);
    }
    
    @Override
    public void logEvent(GameLogEntry entry) {
        // Transform and delegate to the adaptee
        fileLogger.writeLog(
            entry.getTimestamp(),
            entry.getSeverity(),
            entry.getEventType(),
            entry.getMessage()
        );
    }
}