package com.javakaian.shooter.iterator;

public interface Iterator<T> {
    
    /**
     * Reset iterator to first element
     */
    void first();
    
    /**
     * Move to next element
     */
    void next();
    
    /**
     * Check if traversal is complete
     * @return true if no more elements
     */
    boolean isDone();
    
    /**
     * Get current element
     * @return current element or null if done
     */
    T currentItem();
}