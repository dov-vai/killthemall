package com.javakaian.shooter.command;

import java.util.Stack;

/**
 * Manages the history of executed commands and provides undo functionality.
 * Singleton pattern ensures all input handlers share the same command history.
 */
public class CommandHistory {
    private static CommandHistory instance;
    private final Stack<InputCommand> history;
    private final int maxHistorySize;
    
    private CommandHistory() {
        this(50); // Default max history size
    }
    
    private CommandHistory(int maxHistorySize) {
        this.history = new Stack<>();
        this.maxHistorySize = maxHistorySize;
    }
    
    /**
     * Get the singleton instance of CommandHistory
     */
    public static synchronized CommandHistory getInstance() {
        if (instance == null) {
            instance = new CommandHistory();
        }
        return instance;
    }
    
    /**
     * Execute a command and add it to history if it can be undone
     */
    public void executeCommand(InputCommand command) {
        command.execute();
        
        if (command.canUndo()) {
            history.push(command);
            
            // Limit history size to prevent memory issues
            if (history.size() > maxHistorySize) {
                history.remove(0);
            }
        }
    }
    
    /**
     * Undo the last command that can be undone
     * @return true if a command was undone, false if history is empty or command cannot be undone
     */
    public boolean undo() {
        while (!history.isEmpty()) {
            InputCommand command = history.pop();
            if (command.canUndo()) {
                command.undo();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Clear all command history
     */
    public void clear() {
        history.clear();
    }
    
    /**
     * Get the number of commands in history
     */
    public int size() {
        return history.size();
    }
    
    /**
     * Check if there are any commands that can be undone
     */
    public boolean canUndo() {
        return history.stream().anyMatch(InputCommand::canUndo);
    }
}
