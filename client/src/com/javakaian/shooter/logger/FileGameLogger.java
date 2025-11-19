package com.javakaian.shooter.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileGameLogger {

    private String filePath;
    private SimpleDateFormat dateFormat;

    public FileGameLogger(String filePath) {
        this.filePath = filePath;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    public void writeLog(long timestamp, String severity, String eventType, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String formattedTime = dateFormat.format(new Date(timestamp));
            String logLine = String.format("[%s] [%s] [%s] %s%n",
                    formattedTime, severity, eventType, message);
            writer.write(logLine);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }


    public void writeExceptionLog(long timestamp, String severity, String eventType,
                                  String message, Exception exception) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String formattedTime = dateFormat.format(new Date(timestamp));
            String logLine = String.format("[%s] [%s] [%s] %s - Exception: %s%n",
                    formattedTime, severity, eventType, message, exception.getMessage());
            writer.write(logLine);
        } catch (IOException e) {
            System.err.println("Failed to write exception to log file: " + e.getMessage());
        }
    }


    public boolean validateFilePath() {
        return filePath != null && !filePath.isEmpty();
    }


    public String getFilePath() {
        return filePath;
    }


    public void setFilePath(String newPath) {
        this.filePath = newPath;
    }
}