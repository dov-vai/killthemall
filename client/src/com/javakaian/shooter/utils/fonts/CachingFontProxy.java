package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import org.apache.log4j.Logger;

public class CachingFontProxy implements FontResource {
    
    private static final Logger logger = Logger.getLogger(CachingFontProxy.class);
    private static int cacheHits = 0;
    private static int cacheMisses = 0;
    
    private final int size;
    private final Color color;
    private final String fontPath;
    private final String identifier;
    
    private RealFontResource realSubject;
    private BitmapFont cachedFont; // Added functionality: cache
    private int accessCount = 0;
    private long totalAccessTime = 0;
    
    public CachingFontProxy(int size, Color color, String fontPath, String identifier) {
        this.size = size;
        this.color = color;
        this.fontPath = fontPath;
        this.identifier = identifier;
        
        logger.info(String.format("[%s] CachingProxy: Created with caching functionality", identifier));
    }
    
    @Override
    public BitmapFont getFont() {
        long startTime = System.nanoTime();
        accessCount++;
        
        // Check cache first (ADDED FUNCTIONALITY)
        if (cachedFont != null) {
            cacheHits++;
            long duration = System.nanoTime() - startTime;
            totalAccessTime += duration;
            
            // REDUCED LOGGING - only log every 100th hit or during demo
            if (cacheHits % 100 == 0) {
                logger.debug(String.format("[%s] CachingProxy: Cache hit #%d", identifier, cacheHits));
            }
            
            return cachedFont;
        }
        
        // Cache miss - create real subject
        cacheMisses++;
        logger.info(String.format("[%s] CachingProxy: Cache miss, creating font... [Total misses: %d]", 
            identifier, cacheMisses));
        
        if (realSubject == null) {
            realSubject = new RealFontResource(size, color, fontPath, identifier);
        }
        
        cachedFont = realSubject.getFont();
        
        long duration = System.nanoTime() - startTime;
        totalAccessTime += duration;
        
        logger.info(String.format("[%s] CachingProxy: Font cached! Time: %.2fms", 
            identifier, duration / 1_000_000.0));
        
        return cachedFont;
    }
    
    @Override
    public void dispose() {
        if (realSubject != null) {
            logger.info(String.format("[%s] CachingProxy: Disposing (accessed %d times, avg time: %.2fμs)", 
                identifier, accessCount, 
                accessCount > 0 ? totalAccessTime / (1000.0 * accessCount) : 0));
            realSubject.dispose();
            cachedFont = null;
        }
    }
    
    @Override
    public boolean isLoaded() {
        return cachedFont != null;
    }
    
    //Get performance statistics (ADDED FUNCTIONALITY)
    public int getAccessCount() {
        return accessCount;
    }
    
    public double getAverageAccessTimeMicros() {
        return accessCount > 0 ? totalAccessTime / (1000.0 * accessCount) : 0;
    }
    
    public static int getCacheHits() {
        return cacheHits;
    }
    
    public static int getCacheMisses() {
        return cacheMisses;
    }
    
    public static double getCacheHitRatio() {
        int total = cacheHits + cacheMisses;
        return total > 0 ? (double) cacheHits / total : 0;
    }
}