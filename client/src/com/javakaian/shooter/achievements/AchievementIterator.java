package com.javakaian.shooter.achievements;

/**
 * Iterator Pattern - Iterator Interface
 * Defines the interface for traversing Achievement collections
 */
public interface AchievementIterator {
    /**
     * Check if there are more achievements to iterate
     * @return true if there are more elements
     */
    boolean hasNext();
    
    /**
     * Get the next achievement in the collection
     * @return the next Achievement
     */
    Achievement next();
    
    /**
     * Get the current achievement without advancing
     * @return the current Achievement or null if no current
     */
    Achievement current();
    
    /**
     * Reset the iterator to the beginning
     */
    void reset();
}
