package com.javakaian.shooter.iterator;

import com.javakaian.shooter.shapes.PowerUp;

/**
 * Concrete Aggregate using Array as internal data structure.
 * Demonstrates iteration over fixed-size array.
 */
public class PowerUpArray implements PowerUpCollection {
    
    private PowerUp[] powerUps;
    private int count;
    private static final int INITIAL_CAPACITY = 10;
    
    public PowerUpArray() {
        this.powerUps = new PowerUp[INITIAL_CAPACITY];
        this.count = 0;
    }
    
    @Override
    public Iterator<PowerUp> createIterator() {
        return new ArrayIterator(this);
    }
    
    @Override
    public void add(PowerUp powerUp) {
        if (count >= powerUps.length) {
            resize();
        }
        powerUps[count++] = powerUp;
    }
    
    @Override
    public void remove(PowerUp powerUp) {
        for (int i = 0; i < count; i++) {
            if (powerUps[i] != null && powerUps[i].getId() == powerUp.getId()) {
                // Shift elements left
                for (int j = i; j < count - 1; j++) {
                    powerUps[j] = powerUps[j + 1];
                }
                powerUps[--count] = null;
                return;
            }
        }
    }
    
    @Override
    public int size() {
        return count;
    }
    
    @Override
    public void clear() {
        powerUps = new PowerUp[INITIAL_CAPACITY];
        count = 0;
    }
    
    private void resize() {
        PowerUp[] newArray = new PowerUp[powerUps.length * 2];
        System.arraycopy(powerUps, 0, newArray, 0, powerUps.length);
        powerUps = newArray;
    }
    
    // Package-private getters for ArrayIterator to access internal structure
    PowerUp[] getPowerUps() {
        return powerUps;
    }
    
    int getCount() {
        return count;
    }
}