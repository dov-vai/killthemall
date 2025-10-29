package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

/**
 * Implementation interface for the Bridge Pattern.
 * Defines the firing behavior that can be decoupled from control modes.
 * 
 * This is the Implementor in the Bridge Pattern.
 */
public interface FiringMechanism {
    
    /**
     * Fire bullets from the given position in the specified direction.
     * 
     * @param position The position to fire from
     * @param direction The direction to fire in (normalized vector)
     * @param player The player firing the weapon
     */
    void fire(Vector2 position, Vector2 direction, Player player);
    
    /**
     * Reload the weapon's ammunition.
     */
    void reload();
    
    /**
     * Check if the weapon can currently fire.
     * 
     * @return true if the weapon can fire, false otherwise
     */
    boolean canFire();
    
    /**
     * Get the fire rate of this mechanism in bullets per second.
     * 
     * @return The fire rate
     */
    float getFireRate();
    
    /**
     * Get the current ammunition count.
     * 
     * @return The current ammo count
     */
    int getAmmoCount();
    
    /**
     * Update the firing mechanism state (cooldowns, etc.).
     * 
     * @param deltaTime Time since last update
     */
    void update(float deltaTime);
}
