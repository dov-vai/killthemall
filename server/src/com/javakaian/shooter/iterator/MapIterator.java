package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Iterator for HashMap-based collection.
 * Iterates through map values in arbitrary order.
 */
public class MapIterator implements Iterator<PowerUp> {
    
    private PowerUpMap collection;
    private List<Integer> processedIds;
    private PowerUp currentPowerUp;
    private boolean isFirstCall;
    
    public MapIterator(PowerUpMap collection) {
        this.collection = collection;
        this.processedIds = new ArrayList<>();
        this.currentPowerUp = null;
        this.isFirstCall = true;
    }
    
    @Override
    public void first() {
        processedIds.clear();
        isFirstCall = true;
        findNextByPriority();
    }
    
    @Override
    public void next() {
        if (isFirstCall) {
            isFirstCall = false;
            return;
        }
        
        if (currentPowerUp != null) {
            processedIds.add(currentPowerUp.getId());
        }
        findNextByPriority();
    }

    private void findNextByPriority() {
        PowerUp lowestIdPowerUp = null;
        int lowestId = Integer.MAX_VALUE;
        
        // search through all powerUps to find next lowest ID
        for (PowerUp powerUp : collection.getPowerUps().values()) {
            if (powerUp != null && 
                !processedIds.contains(powerUp.getId()) && 
                powerUp.getId() < lowestId) {
                
                lowestId = powerUp.getId();
                lowestIdPowerUp = powerUp;
            }
        }
        
        currentPowerUp = lowestIdPowerUp;
    }
    
    @Override
    public boolean isDone() {
        return currentPowerUp == null;
    }
    
    @Override
    public PowerUp currentItem() {
        return currentPowerUp;
    }
}