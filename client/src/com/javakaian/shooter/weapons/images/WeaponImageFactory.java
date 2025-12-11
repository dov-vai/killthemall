package com.javakaian.shooter.weapons.images;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight Pattern - Factory
 * Manages the pool of shared WeaponImage flyweights
 * Ensures that weapon images are shared and reused rather than duplicated
 */
public class WeaponImageFactory {
    
    private static final Logger logger = Logger.getLogger(WeaponImageFactory.class);
    private static WeaponImageFactory instance;
    
    // Flyweight pool - stores shared weapon images
    private final Map<String, WeaponImage> imagePool = new HashMap<>();
    
    // Statistics
    private int totalRequests = 0;
    private int cacheHits = 0;
    private int cacheMisses = 0;
    
    // Default texture paths for weapons
    private static final Map<String, String> DEFAULT_PATHS = new HashMap<>();
    
    static {
        DEFAULT_PATHS.put("assault_rifle", "weapons/assault_rifle.png");
        DEFAULT_PATHS.put("combat_shotgun", "weapons/combat_shotgun.png");
        DEFAULT_PATHS.put("precision_sniper", "weapons/precision_sniper.png");
        DEFAULT_PATHS.put("pistol", "weapons/pistol.png");
        DEFAULT_PATHS.put("smg", "weapons/smg.png");
    }
    
    private WeaponImageFactory() {
        logger.info("[WeaponImageFactory] Flyweight factory initialized");
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized WeaponImageFactory getInstance() {
        if (instance == null) {
            instance = new WeaponImageFactory();
        }
        return instance;
    }
    
    /**
     * Get a weapon image - returns shared instance (Flyweight)
     * This is the key method of the Flyweight pattern
     * 
     * @param weaponName The weapon name/type
     * @return Shared WeaponImage instance
     */
    public WeaponImage getWeaponImage(String weaponName) {
        totalRequests++;
        
        // Check if we already have this image (Flyweight pool lookup)
        if (imagePool.containsKey(weaponName)) {
            cacheHits++;
            logger.debug(String.format("[WeaponImageFactory] Cache HIT for '%s' (Total hits: %d/%d)", 
                weaponName, cacheHits, totalRequests));
            return imagePool.get(weaponName);
        }
        
        // Cache miss - create new flyweight
        cacheMisses++;
        logger.info(String.format("[WeaponImageFactory] Cache MISS for '%s' - Creating new flyweight (Total misses: %d/%d)", 
            weaponName, cacheMisses, totalRequests));
        
        String texturePath = DEFAULT_PATHS.getOrDefault(weaponName, "badlogic.jpg");
        WeaponImage newImage = new ConcreteWeaponImage(weaponName, texturePath);
        
        // Store in pool for future reuse
        imagePool.put(weaponName, newImage);
        
        logger.info(String.format("[WeaponImageFactory] Added '%s' to flyweight pool (Pool size: %d)", 
            weaponName, imagePool.size()));
        
        return newImage;
    }
    
    /**
     * Get weapon image with custom texture path
     */
    public WeaponImage getWeaponImage(String weaponName, String customTexturePath) {
        totalRequests++;
        
        // For custom paths, use weaponName as key but may create new if path differs
        String key = weaponName + "_" + customTexturePath.hashCode();
        
        if (imagePool.containsKey(key)) {
            cacheHits++;
            return imagePool.get(key);
        }
        
        cacheMisses++;
        WeaponImage newImage = new ConcreteWeaponImage(weaponName, customTexturePath);
        imagePool.put(key, newImage);
        
        return newImage;
    }
    
    /**
     * Preload weapon images to avoid lag during gameplay
     */
    public void preloadWeaponImages(String... weaponNames) {
        logger.info(String.format("[WeaponImageFactory] Preloading %d weapon images...", weaponNames.length));
        long startTime = System.nanoTime();
        
        for (String weaponName : weaponNames) {
            WeaponImage image = getWeaponImage(weaponName);
            // Trigger loading by accessing texture
            image.getTexture();
        }
        
        long duration = System.nanoTime() - startTime;
        logger.info(String.format("[WeaponImageFactory] Preloading completed in %.2f ms", duration / 1_000_000.0));
    }
    
    /**
     * Get statistics about flyweight usage
     */
    public FlyweightStats getStats() {
        return new FlyweightStats(
            totalRequests,
            cacheHits,
            cacheMisses,
            imagePool.size(),
            cacheHits > 0 ? (double) cacheHits / totalRequests * 100 : 0
        );
    }
    
    /**
     * Clear the flyweight pool and dispose all textures
     */
    public void clearPool() {
        logger.info(String.format("[WeaponImageFactory] Clearing flyweight pool (%d images)", imagePool.size()));
        
        for (WeaponImage image : imagePool.values()) {
            image.dispose();
        }
        
        imagePool.clear();
        logger.info("[WeaponImageFactory] Pool cleared");
    }
    
    /**
     * Reset statistics (useful for benchmarking)
     */
    public void resetStats() {
        totalRequests = 0;
        cacheHits = 0;
        cacheMisses = 0;
        logger.info("[WeaponImageFactory] Statistics reset");
    }
    
    /**
     * Get current pool size
     */
    public int getPoolSize() {
        return imagePool.size();
    }
    
    /**
     * Statistics data class
     */
    public static class FlyweightStats {
        public final int totalRequests;
        public final int cacheHits;
        public final int cacheMisses;
        public final int poolSize;
        public final double hitRatio;
        
        public FlyweightStats(int totalRequests, int cacheHits, int cacheMisses, 
                            int poolSize, double hitRatio) {
            this.totalRequests = totalRequests;
            this.cacheHits = cacheHits;
            this.cacheMisses = cacheMisses;
            this.poolSize = poolSize;
            this.hitRatio = hitRatio;
        }
        
        @Override
        public String toString() {
            return String.format(
                "FlyweightStats{requests=%d, hits=%d, misses=%d, poolSize=%d, hitRatio=%.1f%%}",
                totalRequests, cacheHits, cacheMisses, poolSize, hitRatio
            );
        }
    }
}
