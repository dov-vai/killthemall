package com.javakaian.shooter.command;

public interface Command {
    void execute();
    
    void undo();
    
    boolean canUndo();
}
