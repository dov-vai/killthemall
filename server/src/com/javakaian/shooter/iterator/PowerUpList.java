package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;
import java.util.LinkedList;

/**
 * Concrete Aggregate using LinkedList as internal data structure.
 * Demonstrates iteration over linked structure.
 */
public class PowerUpList implements PowerUpCollection {
    
    private LinkedList<PowerUp> powerUps;
    
    public PowerUpList() {
        this.powerUps = new LinkedList<>();
    }
    
    @Override
    public Iterator<PowerUp> createIterator() {
        return new ListIterator(this);
    }
    
    @Override
    public void add(PowerUp powerUp) {
        powerUps.add(powerUp);
    }
    
    @Override
    public void remove(PowerUp powerUp) {
        powerUps.removeIf(p -> p.getId() == powerUp.getId());
    }
    
    @Override
    public int size() {
        return powerUps.size();
    }
    
    @Override
    public void clear() {
        powerUps.clear();
    }
    
    // Package-private getter for ListIterator to access internal structure
    LinkedList<PowerUp> getPowerUps() {
        return powerUps;
    }
}