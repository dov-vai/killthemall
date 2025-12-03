package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.graphics.Color;
import org.apache.log4j.Logger;

public class FontManager {
    
    private static final Logger logger = Logger.getLogger(FontManager.class);
    
    private final int size;
    private final Color color;
    private final String fontPath;
    private final String identifier;
    
    private FontResource currentProxy; // can be switched at runtime
    private ProxyType currentType;
    
    public enum ProxyType {
        VIRTUAL,    // Delayed creation
        SECURITY,   // Access control
        CACHING     // Added functionality
    }
    
    public FontManager(int size, Color color, String fontPath, String identifier) {
        this.size = size;
        this.color = color;
        this.fontPath = fontPath;
        this.identifier = identifier;
        
        // start with Virtual proxy by default
        switchToVirtualProxy();
    }
    
    /**
     * RUNTIME SWITCH: Change to Virtual Proxy (delayed creation)
     */
    public void switchToVirtualProxy() {
        logger.info(String.format("[%s] FontManager: SWITCHING TO VIRTUAL PROXY (delayed creation)", identifier));
        
        // Dispose old proxy if exists
        if (currentProxy != null) {
            currentProxy.dispose();
        }
        
        currentProxy = new VirtualFontProxy(size, color, fontPath, identifier);
        currentType = ProxyType.VIRTUAL;
    }
    
    /**
     * RUNTIME SWITCH: Change to Security Proxy (access control)
     */
    public void switchToSecurityProxy(boolean initialAccess) {
        logger.info(String.format("[%s] FontManager: SWITCHING TO SECURITY PROXY (access control)", identifier));
        
        // Dispose old proxy if exists
        if (currentProxy != null) {
            currentProxy.dispose();
        }
        
        currentProxy = new SecurityFontProxy(size, color, fontPath, identifier, initialAccess);
        currentType = ProxyType.SECURITY;
    }
    
    /**
     * RUNTIME SWITCH: Change to Caching Proxy (added functionality)
     */
    public void switchToCachingProxy() {
        logger.info(String.format("[%s] FontManager: SWITCHING TO CACHING PROXY (added functionality)", identifier));
        
        // Dispose old proxy if exists
        if (currentProxy != null) {
            currentProxy.dispose();
        }
        
        currentProxy = new CachingFontProxy(size, color, fontPath, identifier);
        currentType = ProxyType.CACHING;
    }
    
    public FontResource getFontResource() {
        return currentProxy;
    }
    
    public ProxyType getCurrentType() {
        return currentType;
    }
    
    // change access at runtime
    public void setSecurityAccess(boolean allowed) {
        if (currentProxy instanceof SecurityFontProxy) {
            ((SecurityFontProxy) currentProxy).setAccessAllowed(allowed);
        } else {
            logger.warn(String.format("[%s] Cannot set security access - current proxy is %s", 
                identifier, currentType));
        }
    }
    
    public void dispose() {
        if (currentProxy != null) {
            currentProxy.dispose();
        }
    }
}