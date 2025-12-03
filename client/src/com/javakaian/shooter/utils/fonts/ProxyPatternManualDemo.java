package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.utils.fonts.ProxyPerformanceReport;

/**
 * Manual Proxy Pattern Demonstration
 * Run from game by pressing F9 key
 */
public class ProxyPatternManualDemo {
    
    public static void runFullDemonstration() {
        System.out.println("\n=== PROXY PATTERN DEMONSTRATION ===\n");
        
        FontManager fontManager = new FontManager(30, Color.WHITE, "Warungasem.ttf", "DemoFont");
        
        // Part 1: Virtual Proxy
        demonstrateVirtualProxy(fontManager);
        
        // Part 2: Security Proxy  
        demonstrateSecurityProxy(fontManager);
        
        // Part 3: Caching Proxy
        demonstrateCachingProxy(fontManager);
        
        // Statistics
        printStatistics();
        
        fontManager.dispose();
        System.out.println("\n=== DEMONSTRATION COMPLETE ===\n");
        System.out.println("Check logs/killthemall-client.log for detailed output");
    }
    
    private static void demonstrateVirtualProxy(FontManager fontManager) {
        System.out.println("--- VIRTUAL PROXY: Delayed Creation ---");
        
        fontManager.switchToVirtualProxy();
        FontResource proxy = fontManager.getFontResource();
        
        System.out.println("Proxy created. Font loaded: " + proxy.isLoaded());
        
        System.out.println("First access - creating font...");
        long start = System.currentTimeMillis();
        BitmapFont font = proxy.getFont();
        long duration = System.currentTimeMillis() - start;
        
        System.out.println("Font created: " + (font != null));
        System.out.println("Font loaded: " + proxy.isLoaded());
        System.out.println("Creation time: " + duration + "ms");
        
        System.out.println("Second access - instant...");
        start = System.currentTimeMillis();
        BitmapFont font2 = proxy.getFont();
        duration = System.currentTimeMillis() - start;
        
        System.out.println("Same object: " + (font == font2));
        System.out.println("Access time: " + duration + "ms\n");
    }
    
    private static void demonstrateSecurityProxy(FontManager fontManager) {
        System.out.println("--- SECURITY PROXY: Access Control ---");
        
        fontManager.switchToSecurityProxy(true);
        
        System.out.println("Access ALLOWED - requesting font...");
        BitmapFont font1 = fontManager.getFontResource().getFont();
        System.out.println("Result: " + (font1 != null ? "Font received" : "Access denied"));
        System.out.println("Access granted: " + SecurityFontProxy.getAccessGrantedCount());
        
        System.out.println("\nChanging access to DENIED...");
        fontManager.setSecurityAccess(false);
        BitmapFont font2 = fontManager.getFontResource().getFont();
        System.out.println("Result: " + (font2 == null ? "Access denied" : "Font received"));
        System.out.println("Access denied: " + SecurityFontProxy.getAccessDeniedCount());
        
        System.out.println("\nChanging access to ALLOWED...");
        fontManager.setSecurityAccess(true);
        BitmapFont font3 = fontManager.getFontResource().getFont();
        System.out.println("Result: " + (font3 != null ? "Font received" : "Access denied"));
        System.out.println("Access granted: " + SecurityFontProxy.getAccessGrantedCount() + "\n");
    }
    
    private static void demonstrateCachingProxy(FontManager fontManager) {
        System.out.println("--- CACHING PROXY: Added Functionality ---");
        
        fontManager.switchToCachingProxy();
        FontResource proxy = fontManager.getFontResource();
        
        System.out.println("First access - cache miss...");
        long start = System.nanoTime();
        BitmapFont font1 = proxy.getFont();
        long firstAccess = System.nanoTime() - start;
        
        System.out.println("Font created: " + (font1 != null));
        System.out.println("Time: " + String.format("%.2fms", firstAccess / 1_000_000.0));
        System.out.println("Cache misses: " + CachingFontProxy.getCacheMisses());
        
        System.out.println("\nSecond access - cache hit...");
        start = System.nanoTime();
        BitmapFont font2 = proxy.getFont();
        long secondAccess = System.nanoTime() - start;
        
        System.out.println("Same object: " + (font1 == font2));
        System.out.println("Time: " + String.format("%.2fus", secondAccess / 1000.0));
        System.out.println("Cache hits: " + CachingFontProxy.getCacheHits());
        
        System.out.println("\nThird access - cache hit...");
        proxy.getFont();
        System.out.println("Total cache hits: " + CachingFontProxy.getCacheHits());
        System.out.println("Hit ratio: " + String.format("%.1f%%", CachingFontProxy.getCacheHitRatio() * 100) + "\n");
    }
    
    private static void printStatistics() {
        System.out.println("--- FINAL STATISTICS ---");
        System.out.println("Virtual Proxy - Delayed creations: " + VirtualFontProxy.getDelayedCreationCount());
        System.out.println("Security Proxy - Access granted: " + SecurityFontProxy.getAccessGrantedCount());
        System.out.println("Security Proxy - Access denied: " + SecurityFontProxy.getAccessDeniedCount());
        System.out.println("Caching Proxy - Cache hits: " + CachingFontProxy.getCacheHits());
        System.out.println("Caching Proxy - Cache misses: " + CachingFontProxy.getCacheMisses());
    }

    public static void runPerformanceReport() {
        ProxyPerformanceReport.generateFullReport();
    }
}