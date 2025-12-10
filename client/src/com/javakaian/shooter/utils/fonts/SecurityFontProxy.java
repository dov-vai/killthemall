package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import org.apache.log4j.Logger;

public class SecurityFontProxy implements FontResource {
    
    private static final Logger logger = Logger.getLogger(SecurityFontProxy.class);
    private static int accessDeniedCount = 0;
    private static int accessGrantedCount = 0;
    
    private final int size;
    private final Color color;
    private final String fontPath;
    private final String identifier;
    
    private RealFontResource realSubject;
    private boolean accessAllowed = true; // Can be changed at runtime
    
    public SecurityFontProxy(int size, Color color, String fontPath, String identifier, boolean initialAccess) {
        this.size = size;
        this.color = color;
        this.fontPath = fontPath;
        this.identifier = identifier;
        this.accessAllowed = initialAccess;
        
        logger.info(String.format("[%s] SecurityProxy: Created (access=%s)", 
            identifier, accessAllowed ? "ALLOWED" : "DENIED"));
    }
    
    /**
     * Change access permission at RUNTIME
     */
    public void setAccessAllowed(boolean allowed) {
        logger.info(String.format("[%s] SecurityProxy: Access changed from %s to %s", 
            identifier, 
            accessAllowed ? "ALLOWED" : "DENIED",
            allowed ? "ALLOWED" : "DENIED"));
        this.accessAllowed = allowed;
    }
    
    @Override
    public BitmapFont getFont() {
        // SECURITY CHECK
        if (!accessAllowed) {
            accessDeniedCount++;
            logger.warn(String.format("[%s] SecurityProxy: ACCESS DENIED (total: %d)", 
                identifier, accessDeniedCount));
            return null;
        }
        
        accessGrantedCount++;
        
        // REDUCED LOGGING - only log first access and every 100th access
        if (accessGrantedCount == 1 || accessGrantedCount % 100 == 0) {
            logger.debug(String.format("[%s] SecurityProxy: Access granted (total: %d)", 
                identifier, accessGrantedCount));
        }
        
        // Create real subject if needed
        if (realSubject == null) {
            logger.info(String.format("[%s] SecurityProxy: Creating RealSubject", identifier));
            realSubject = new RealFontResource(size, color, fontPath, identifier);
        }
        
        return realSubject.getFont();
    }
    
    @Override
    public void dispose() {
        if (realSubject != null) {
            logger.info(String.format("[%s] SecurityProxy: Disposing", identifier));
            realSubject.dispose();
        }
    }
    
    @Override
    public boolean isLoaded() {
        return realSubject != null && realSubject.isLoaded();
    }
    
    public static int getAccessDeniedCount() {
        return accessDeniedCount;
    }
    
    public static int getAccessGrantedCount() {
        return accessGrantedCount;
    }
}