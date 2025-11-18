package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;

/**
 * Concrete Iterator for Array-based collection.
 * Iterates through array from index 0 to count.
 */
public class ArrayIterator implements Iterator<PowerUp> {
    
    private PowerUpArray collection;
    private int currentIndex;
    
    public ArrayIterator(PowerUpArray collection) {
        this.collection = collection;
        this.currentIndex = 0;
    }
    
    @Override
    public void first() {
        currentIndex = 0;
    }
    
    @Override
    public void next() {
        if (!isDone()) {
            currentIndex++;
        }
    }
    
    @Override
    public boolean isDone() {
        return currentIndex >= collection.getCount();
    }
    
    @Override
    public PowerUp currentItem() {
        if (isDone()) {
            return null;
        }
        return collection.getPowerUps()[currentIndex];
    }
}