package com.javakaian.shooter.weapons.images;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.apache.log4j.Logger;

/**
 * Flyweight Pattern - Concrete Flyweight
 * Represents the shared intrinsic state (weapon texture)
 * This is the object that will be shared across multiple weapon instances
 */
public class ConcreteWeaponImage implements WeaponImage {
    
    private static final Logger logger = Logger.getLogger(ConcreteWeaponImage.class);
    
    private final String weaponName;
    private final String texturePath;
    private Texture texture;
    private boolean loaded = false;
    private long loadTime = 0;
    
    public ConcreteWeaponImage(String weaponName, String texturePath) {
        this.weaponName = weaponName;
        this.texturePath = texturePath;
    }
    
    @Override
    public Texture getTexture() {
        if (!loaded) {
            loadTexture();
        }
        return texture;
    }
    
    private void loadTexture() {
        long startTime = System.nanoTime();
        
        logger.info(String.format("[WeaponImage] Loading texture for '%s' from '%s'", 
            weaponName, texturePath));
        
        try {
            texture = new Texture(Gdx.files.internal(texturePath));
            loaded = true;
            loadTime = System.nanoTime() - startTime;
            
            logger.info(String.format("[WeaponImage] Loaded '%s' in %.2f ms", 
                weaponName, loadTime / 1_000_000.0));
        } catch (Exception e) {
            logger.error(String.format("[WeaponImage] Failed to load texture for '%s': %s", 
                weaponName, e.getMessage()));
            // Create a simple 1x1 white texture as fallback
            texture = createFallbackTexture();
            loaded = true;
        }
    }
    
    private Texture createFallbackTexture() {
        logger.warn(String.format("[WeaponImage] Creating fallback texture for '%s'", weaponName));
        // Use existing badlogic.jpg as fallback
        try {
            return new Texture(Gdx.files.internal("badlogic.jpg"));
        } catch (Exception e) {
            logger.error("[WeaponImage] Even fallback texture failed!");
            return null;
        }
    }
    
    @Override
    public String getWeaponName() {
        return weaponName;
    }
    
    @Override
    public void dispose() {
        if (texture != null && loaded) {
            logger.info(String.format("[WeaponImage] Disposing texture for '%s'", weaponName));
            texture.dispose();
            loaded = false;
        }
    }
    
    @Override
    public boolean isLoaded() {
        return loaded;
    }
    
    public long getLoadTime() {
        return loadTime;
    }
}
