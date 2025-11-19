package com.javakaian.shooter.command;

/**
 * Interface for input commands that can be executed and potentially undone.
 * This allows for flexible key binding and undo functionality.
 */
public interface InputCommand {
    /**
     * Execute the command
     */
    void execute();

    /**
     * Undo the command if possible
     */
    void undo();

    /**
     * Check if this command can be undone
     *
     * @return true if the command can be undone
     */
    boolean canUndo();

    /**
     * Get a description of what this command does
     *
     * @return command description
     */
    String getDescription();
}
