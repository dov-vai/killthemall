package com.javakaian.shooter.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * Caretaker class for managing Player mementos.
 * 
 * The Caretaker is responsible for:
 * - Storing the collection of mementos (checkpoints)
 * - Providing undo/redo functionality
 * - Managing memento lifecycle
 * 
 * The Caretaker ONLY sees the Narrow Interface (IMemento) and cannot
 * access the internal state of mementos - this ensures security!
 * 
 * Think of this like a "Save Game Manager" that keeps track of all your
 * saved checkpoints but doesn't know what's inside them.
 */
public class PlayerCaretaker {
    
    private final List<IMemento> mementoList;
    private final int playerId;
    private int currentIndex = -1;
    
    public PlayerCaretaker(int playerId) {
        this.mementoList = new ArrayList<>();
        this.playerId = playerId;
    }
    
    /**
     * Save a checkpoint (add memento to history).
     * Like pressing "Save Game" at a checkpoint.
     */
    public void saveCheckpoint(IMemento memento) {
        // If we're not at the end of history, remove future states
        // (like when you load an old save and make new progress)
        if (currentIndex < mementoList.size() - 1) {
            mementoList.subList(currentIndex + 1, mementoList.size()).clear();
        }
        
        mementoList.add(memento);
        currentIndex++;
        
        System.out.println("Checkpoint saved for Player " + playerId + 
                         " (Total checkpoints: " + mementoList.size() + ")");
    }
    
    /**
     * Restore to a specific checkpoint by index.
     * Returns null if index is invalid.
     */
    public IMemento restoreToCheckpoint(int checkpointIndex) {
        if (checkpointIndex < 0 || checkpointIndex >= mementoList.size()) {
            System.out.println("Invalid checkpoint index: " + checkpointIndex);
            return null;
        }
        
        currentIndex = checkpointIndex;
        System.out.println("Restoring Player " + playerId + 
                         " to checkpoint " + checkpointIndex);
        
        return mementoList.get(checkpointIndex);
    }
    
    /**
     * Undo to previous state (like Ctrl+Z).
     * Returns the previous memento or null if at the beginning.
     */
    public IMemento undo() {
        if (currentIndex <= 0) {
            System.out.println("Cannot undo - at first checkpoint");
            return null;
        }
        
        currentIndex--;
        System.out.println("Undo: Player " + playerId + 
                         " restored to checkpoint " + currentIndex);
        
        return mementoList.get(currentIndex);
    }
    
    /**
     * Redo to next state (like Ctrl+Y).
     * Returns the next memento or null if at the end.
     */
    public IMemento redo() {
        if (currentIndex >= mementoList.size() - 1) {
            System.out.println("Cannot redo - at latest checkpoint");
            return null;
        }
        
        currentIndex++;
        System.out.println("Redo: Player " + playerId + 
                         " restored to checkpoint " + currentIndex);
        
        return mementoList.get(currentIndex);
    }
    
    /**
     * Get the most recent checkpoint without changing current position.
     */
    public IMemento getLastCheckpoint() {
        if (mementoList.isEmpty()) {
            return null;
        }
        return mementoList.get(mementoList.size() - 1);
    }
    
    /**
     * Clear all checkpoints.
     */
    public void clearHistory() {
        mementoList.clear();
        currentIndex = -1;
        System.out.println("Cleared all checkpoints for Player " + playerId);
    }
    
    /**
     * Get total number of saved checkpoints.
     */
    public int getCheckpointCount() {
        return mementoList.size();
    }
    
    /**
     * Get current checkpoint index.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }
    
    /**
     * Check if undo is available.
     */
    public boolean canUndo() {
        return currentIndex > 0;
    }
    
    /**
     * Check if redo is available.
     */
    public boolean canRedo() {
        return currentIndex < mementoList.size() - 1;
    }
}