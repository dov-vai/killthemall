package com.javakaian.shooter.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogCapture {
    
    private static final LogCapture INSTANCE = new LogCapture();
    private final List<String> logEntries = new ArrayList<>();
    private final int maxEntries = 100;
    
    private LogCapture() {}
    
    public static LogCapture getInstance() {
        return INSTANCE;
    }
    
    public synchronized void addLog(String message) {
        logEntries.add(message);
        if (logEntries.size() > maxEntries) {
            logEntries.remove(0);
        }
    }
    
    public synchronized List<String> getLogs() {
        return new ArrayList<>(logEntries);
    }
    
    public synchronized void clear() {
        logEntries.clear();
    }
}