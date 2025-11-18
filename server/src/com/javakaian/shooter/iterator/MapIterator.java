package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Iterator for HashMap-based collection.
 * Iterates through map values in arbitrary order.
 */
public class MapIterator implements Iterator<PowerUp> {
    
    private List<PowerUp> powerUpList;
    private int currentIndex;
    
    public MapIterator(PowerUpMap collection) {
        // Convert map values to list for iteration
        this.powerUpList = new ArrayList<>(collection.getPowerUps().values());
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
        return currentIndex >= powerUpList.size();
    }
    
    @Override
    public PowerUp currentItem() {
        if (isDone()) {
            return null;
        }
        return powerUpList.get(currentIndex);
    }
}