package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import org.apache.log4j.Logger;

public class VirtualFontProxy implements FontResource {
    
    private static final Logger logger = Logger.getLogger(VirtualFontProxy.class);
    private static int delayedCreationCount = 0;
    
    private final int size;
    private final Color color;
    private final String fontPath;
    private final String identifier;
    
    private RealFontResource realSubject; // Created lazily
    
    public VirtualFontProxy(int size, Color color, String fontPath, String identifier) {
        this.size = size;
        this.color = color;
        this.fontPath = fontPath;
        this.identifier = identifier;
        
        logger.info(String.format("[%s] VirtualProxy: Created (font NOT created yet - delayed creation)", identifier));
    }
    
    @Override
    public BitmapFont getFont() {
        // Lazy initialization - create only when first needed
        if (realSubject == null) {
            logger.info(String.format("[%s] VirtualProxy: First access detected! Creating RealSubject now...", identifier));
            long start = System.currentTimeMillis();
            
            realSubject = new RealFontResource(size, color, fontPath, identifier);
            
            long duration = System.currentTimeMillis() - start;
            delayedCreationCount++;
            logger.info(String.format("[%s] VirtualProxy: Delayed creation completed in %dms (total delayed: %d)", 
                identifier, duration, delayedCreationCount));
        }
        
        return realSubject.getFont();
    }
    
    @Override
    public void dispose() {
        if (realSubject != null) {
            logger.info(String.format("[%s] VirtualProxy: Disposing", identifier));
            realSubject.dispose();
        } else {
            logger.info(String.format("[%s] VirtualProxy: Nothing to dispose (was never created)", identifier));
        }
    }
    
    @Override
    public boolean isLoaded() {
        return realSubject != null && realSubject.isLoaded();
    }
    
    public static int getDelayedCreationCount() {
        return delayedCreationCount;
    }
}