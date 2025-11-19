package com.javakaian.shooter.logger;

public class ConsoleGameLoggerAdapter implements IGameLogger {

    private ConsoleGameLogger consoleLogger;

    public ConsoleGameLoggerAdapter() {
        this.consoleLogger = new ConsoleGameLogger();
        // Enable colors by default for console
        this.consoleLogger.setColorEnabled(true);
    }

    @Override
    public void logEvent(GameLogEntry entry) {
        // Transform GameLogEntry into SimpleLogEntry format
        String fullMessage = String.format("[%s] [%s] %s",
                entry.getSeverity(),
                entry.getEventType(),
                entry.getMessage());

        SimpleLogEntry simpleEntry = new SimpleLogEntry(
                entry.getTimestamp(),
                fullMessage
        );

        // Delegate to the adaptee
        consoleLogger.printLog(simpleEntry);

        LogCapture.getInstance().addLog(fullMessage);
    }
}