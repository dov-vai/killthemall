package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;
import java.util.HashMap;

/**
 * Concrete Aggregate using HashMap as internal data structure.
 * Demonstrates iteration over key-value pairs (ID -> PowerUp).
 */
public class PowerUpMap implements PowerUpCollection {
    
    private HashMap<Integer, PowerUp> powerUps;
    
    public PowerUpMap() {
        this.powerUps = new HashMap<>();
    }
    
    @Override
    public Iterator<PowerUp> createIterator() {
        return new MapIterator(this);
    }
    
    @Override
    public void add(PowerUp powerUp) {
        powerUps.put(powerUp.getId(), powerUp);
    }
    
    @Override
    public void remove(PowerUp powerUp) {
        powerUps.remove(powerUp.getId());
    }
    
    @Override
    public int size() {
        return powerUps.size();
    }
    
    @Override
    public void clear() {
        powerUps.clear();
    }
    
    /**
     * Get PowerUp by ID (HashMap advantage - O(1) lookup)
     */
    public PowerUp getById(int id) {
        return powerUps.get(id);
    }
    
    // Package-private getter for MapIterator to access internal structure
    HashMap<Integer, PowerUp> getPowerUps() {
        return powerUps;
    }
}