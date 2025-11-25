package com.javakaian.shooter.shapes.flyweight;

import com.javakaian.shooter.shapes.PowerUp.PowerUpType;

import java.util.HashMap;
import java.util.Map;

/**
 * FlyweightFactory creates and manages flyweight objects.
 * It ensures that flyweights are shared properly. When a client requests
 * a flyweight, the factory either returns an existing instance or creates
 * a new one if it doesn't exist yet.
 * 
 * This implements the logic shown in the diagram:
 * if(flyweight[key] exists) {
 *     return existing flyweight;
 * } else {
 *     create new flyweight;
 *     add it to a pool of flyweights;
 *     return the new flyweight;
 * }
 */
public class FlyweightFactory {
    
    private Map<PowerUpType, Flyweight> flyweights;
    
    public FlyweightFactory() {
        this.flyweights = new HashMap<>();
    }
    
    /**
     * Returns a flyweight for the given PowerUpType key.
     * If a flyweight for this type already exists, it returns the existing one.
     * Otherwise, it creates a new flyweight, stores it, and returns it.
     * 
     * @param key The PowerUpType that serves as the key for the flyweight
     * @return A shared Flyweight instance for the given type
     */
    public Flyweight getFlyweight(PowerUpType key) {
        if (flyweights.containsKey(key)) {
            // Return existing flyweight
            return flyweights.get(key);
        } else {
            // Create new flyweight
            Flyweight newFlyweight = new ConcreteFlyweight(key);
            // Add it to the pool of flyweights
            flyweights.put(key, newFlyweight);
            // Return the new flyweight
            return newFlyweight;
        }
    }
    
    /**
     * Returns the total number of flyweight instances currently managed.
     * Useful for debugging and verifying that flyweights are being shared properly.
     * 
     * @return The number of unique flyweight instances
     */
    public int getFlyweightCount() {
        return flyweights.size();
    }
    
    /**
     * Clears all cached flyweights.
     * This is useful for testing or when resetting the game state.
     */
    public void clearFlyweights() {
        flyweights.clear();
    }
}
