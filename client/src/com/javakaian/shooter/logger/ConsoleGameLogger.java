package com.javakaian.shooter.logger;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ConsoleGameLogger {
    
    private SimpleDateFormat dateFormat;
    
    public ConsoleGameLogger() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    
    public void printLog(SimpleLogEntry entry) {
        String formattedTime = dateFormat.format(new Date(entry.getTimestamp()));
        System.out.println(String.format("[%s] %s", formattedTime, entry.getFullMessage()));
    }
    
    
    public void printException(ExceptionLogEntry exceptionEntry) {
        String formattedTime = dateFormat.format(new Date(exceptionEntry.getTimestamp()));
        System.err.println(String.format("[%s] ERROR: %s - Exception: %s", 
            formattedTime, 
            exceptionEntry.getFullMessage(),
            exceptionEntry.getExceptionMessage()));
    }
    
    
    public void setColorEnabled(boolean enabled) {
        // This is a specific method for console logger that doesn't exist in FileGameLogger
        System.out.println("Console color output " + (enabled ? "enabled" : "disabled"));
    }
}