package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;

public interface PowerUpCollection {
    
    /**
     * Create an iterator for this collection
     * @return Iterator instance
     */
    Iterator<PowerUp> createIterator();
    
    /**
     * Add a power-up to the collection
     * @param powerUp the power-up to add
     */
    void add(PowerUp powerUp);
    
    /**
     * Remove a power-up from the collection
     * @param powerUp the power-up to remove
     */
    void remove(PowerUp powerUp);
    
    /**
     * Get size of collection
     * @return number of elements
     */
    int size();
    
    /**
     * Clear all elements
     */
    void clear();
}