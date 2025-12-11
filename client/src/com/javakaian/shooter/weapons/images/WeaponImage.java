package com.javakaian.shooter.weapons.images;

import com.badlogic.gdx.graphics.Texture;

/**
 * Flyweight Pattern - Interface for weapon images
 * Represents the shared intrinsic state (the actual texture)
 */
public interface WeaponImage {
    /**
     * Get the texture for this weapon
     * @return The loaded texture
     */
    Texture getTexture();
    
    /**
     * Get the weapon name/type this image represents
     * @return weapon name
     */
    String getWeaponName();
    
    /**
     * Clean up resources
     */
    void dispose();
    
    /**
     * Check if texture is loaded
     * @return true if loaded
     */
    boolean isLoaded();
}
