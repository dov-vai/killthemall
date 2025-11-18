package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;

/**
 * Concrete Iterator for LinkedList-based collection.
 * Iterates through linked list nodes.
 */
public class ListIterator implements Iterator<PowerUp> {
    
    private PowerUpList collection;
    private int currentIndex;
    
    public ListIterator(PowerUpList collection) {
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
        return currentIndex >= collection.getPowerUps().size();
    }
    
    @Override
    public PowerUp currentItem() {
        if (isDone()) {
            return null;
        }
        return collection.getPowerUps().get(currentIndex);
    }
}