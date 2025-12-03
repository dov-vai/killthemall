package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Generates performance and memory usage report for Proxy Pattern
 * For university report (ataskaita)
 */
public class ProxyPerformanceReport {
    
    public static void generateFullReport() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("PROXY PATTERN - PERFORMANCE AND MEMORY USAGE REPORT");
        System.out.println("=".repeat(70));
        
        // Reset counters for accurate measurements
        resetAllCounters();
        
        // Test 1: Virtual Proxy Performance
        testVirtualProxyPerformance();
        
        // Reset counters
        resetAllCounters();
        
        // Test 2: Caching Proxy Performance
        testCachingProxyPerformance();
        
        // Reset counters
        resetAllCounters();
        
        // Test 3: Memory Usage Comparison
        testMemoryUsage();
        
        // Reset counters
        resetAllCounters();
        
        // Test 4: Security Proxy Overhead
        testSecurityProxyOverhead();
        
        System.out.println("=".repeat(70));
        System.out.println("REPORT GENERATION COMPLETE");
        System.out.println("=".repeat(70) + "\n");
    }
    
    private static void resetAllCounters() {
        // Note: Can't reset static counters easily, but we track differences
    }
    
    private static void testVirtualProxyPerformance() {
        System.out.println("\n--- TEST 1: VIRTUAL PROXY - Delayed Creation ---");
        System.out.println("Scenario: Create 10 font proxies, use only 3\n");
        
        FontManager[] managers = new FontManager[10];
        
        System.out.println("STEP 1: Creating 10 Virtual Proxies...");
        long proxyCreationStart = System.nanoTime();
        
        for (int i = 0; i < 10; i++) {
            managers[i] = new FontManager(20 + i * 2, Color.WHITE, "Warungasem.ttf", "VirtualTest" + i);
            managers[i].switchToVirtualProxy();
        }
        
        long proxyCreationTime = System.nanoTime() - proxyCreationStart;
        
        // Check if fonts are loaded
        int fontsLoadedAfterCreation = 0;
        for (FontManager m : managers) {
            if (m.getFontResource().isLoaded()) {
                fontsLoadedAfterCreation++;
            }
        }
        
        System.out.println("  Time to create 10 proxies: " + String.format("%.2fms", proxyCreationTime / 1_000_000.0));
        System.out.println("  Actual fonts created: " + fontsLoadedAfterCreation);
        System.out.println("  Proxies are lightweight placeholders\n");
        
        System.out.println("STEP 2: Accessing 3 out of 10 fonts...");
        long fontAccessStart = System.nanoTime();
        
        BitmapFont font1 = managers[0].getFontResource().getFont();
        BitmapFont font2 = managers[3].getFontResource().getFont();
        BitmapFont font3 = managers[7].getFontResource().getFont();
        
        long fontAccessTime = System.nanoTime() - fontAccessStart;
        
        // Check how many fonts are now loaded
        int fontsLoadedAfterAccess = 0;
        for (FontManager m : managers) {
            if (m.getFontResource().isLoaded()) {
                fontsLoadedAfterAccess++;
            }
        }
        
        System.out.println("  Time to create 3 fonts: " + String.format("%.2fms", fontAccessTime / 1_000_000.0));
        System.out.println("  Fonts now created: " + fontsLoadedAfterAccess + " out of 10");
        System.out.println("  Fonts NOT created: " + (10 - fontsLoadedAfterAccess) + "\n");
        
        // Estimate memory saved
        double avgFontSizeMB = 0.8; // Approximate size per font
        double memorySaved = (10 - fontsLoadedAfterAccess) * avgFontSizeMB;
        
        System.out.println("RESULTS:");
        System.out.println("  Fonts created: " + fontsLoadedAfterAccess + "/10");
        System.out.println("  Memory saved: ~" + String.format("%.1fMB", memorySaved) + 
                          " (" + (10 - fontsLoadedAfterAccess) + " fonts avoided)");
        System.out.println("  Creation speed: " + String.format("%.2fms", proxyCreationTime / 1_000_000.0));
        
        // Cleanup
        for (FontManager m : managers) {
            m.dispose();
        }
    }
    
    private static void testCachingProxyPerformance() {
        System.out.println("\n--- TEST 2: CACHING PROXY - Performance Improvement ---");
        System.out.println("Scenario: Create font once, access it 100 times\n");
        
        // Track cache statistics before
        int hitsBefore = CachingFontProxy.getCacheHits();
        int missesBefore = CachingFontProxy.getCacheMisses();
        
        FontManager manager = new FontManager(30, Color.WHITE, "Warungasem.ttf", "CachingTest");
        manager.switchToCachingProxy();
        
        System.out.println("STEP 1: First access (cache miss - font creation)");
        long firstAccessStart = System.nanoTime();
        BitmapFont font1 = manager.getFontResource().getFont();
        long firstAccessTime = System.nanoTime() - firstAccessStart;
        
        int missesAfterFirst = CachingFontProxy.getCacheMisses() - missesBefore;
        
        System.out.println("  Time: " + String.format("%.2fms", firstAccessTime / 1_000_000.0));
        System.out.println("  Cache misses: " + missesAfterFirst);
        System.out.println("  Font created and cached\n");
        
        System.out.println("STEP 2: Next 100 accesses (cache hits - instant)");
        long[] hitTimes = new long[100];
        
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            BitmapFont font = manager.getFontResource().getFont();
            hitTimes[i] = System.nanoTime() - start;
        }
        
        int hitsAfter = CachingFontProxy.getCacheHits() - hitsBefore;
        
        long totalHitTime = 0;
        for (long t : hitTimes) {
            totalHitTime += t;
        }
        double avgHitTime = totalHitTime / (100.0 * 1000.0); // microseconds
        
        System.out.println("  Average time per access: " + String.format("%.2fμs", avgHitTime));
        System.out.println("  Total time for 100 accesses: " + String.format("%.2fms", totalHitTime / 1_000_000.0));
        System.out.println("  Cache hits: " + hitsAfter + "\n");
        
        double speedup = (firstAccessTime / 1000.0) / avgHitTime;
        
        System.out.println("RESULTS:");
        System.out.println("  First access (miss): " + String.format("%.2fms", firstAccessTime / 1_000_000.0));
        System.out.println("  Cached access (hit): " + String.format("%.2fμs", avgHitTime));
        System.out.println("  Speed improvement: " + String.format("%.0fx FASTER", speedup));
        
        manager.dispose();
    }
    
    private static void testMemoryUsage() {
        System.out.println("\n--- TEST 3: MEMORY USAGE COMPARISON ---");
        System.out.println("Scenario: Game needs 5 font sizes, but only uses 2 during play\n");
        
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Create 5 font managers with Virtual Proxy
        FontManager[] managers = new FontManager[5];
        managers[0] = new FontManager(14, Color.WHITE, "Warungasem.ttf", "UI-Small");
        managers[1] = new FontManager(20, Color.WHITE, "Warungasem.ttf", "UI-Normal");
        managers[2] = new FontManager(28, Color.WHITE, "Warungasem.ttf", "Title-Medium");
        managers[3] = new FontManager(40, Color.WHITE, "Warungasem.ttf", "Title-Large");
        managers[4] = new FontManager(60, Color.WHITE, "Warungasem.ttf", "Logo-Huge");
        
        System.out.println("STEP 1: Create 5 Virtual Proxies");
        for (FontManager m : managers) {
            m.switchToVirtualProxy();
        }
        
        long memoryAfterProxies = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long proxyMemory = memoryAfterProxies - memoryBefore;
        
        // Count loaded fonts
        int loadedBeforeUse = 0;
        for (FontManager m : managers) {
            if (m.getFontResource().isLoaded()) loadedBeforeUse++;
        }
        
        System.out.println("  Proxies created: 5");
        System.out.println("  Fonts created: " + loadedBeforeUse);
        System.out.println("  Memory used: ~" + String.format("%.2fKB", proxyMemory / 1024.0) + "\n");
        
        System.out.println("STEP 2: Use only 2 fonts (typical gameplay)");
        managers[0].getFontResource().getFont(); // UI text
        managers[1].getFontResource().getFont(); // Player health
        
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        long memoryAfterUse = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long totalMemory = memoryAfterUse - memoryBefore;
        
        // Count loaded fonts
        int loadedAfterUse = 0;
        for (FontManager m : managers) {
            if (m.getFontResource().isLoaded()) loadedAfterUse++;
        }
        
        System.out.println("  Fonts now created: " + loadedAfterUse + "/5");
        System.out.println("  Memory used: ~" + String.format("%.2fMB", totalMemory / (1024.0 * 1024.0)) + "\n");
        
        // Estimate savings
        int fontsNotCreated = 5 - loadedAfterUse;
        double avgFontSize = 0.8; // MB per font (estimate)
        double estimatedSavings = fontsNotCreated * avgFontSize;
        
        System.out.println("RESULTS:");
        System.out.println("  Fonts created: " + loadedAfterUse + "/5");
        System.out.println("  Fonts NOT created: " + fontsNotCreated);
        System.out.println("  Memory saved: ~" + String.format("%.1fMB", estimatedSavings));

        // Cleanup
        for (FontManager m : managers) {
            m.dispose();
        }
    }
    
    private static void testSecurityProxyOverhead() {
        System.out.println("\n--- TEST 4: SECURITY PROXY - Access Control Overhead ---");
        System.out.println("Scenario: Measure performance cost of security checks\n");
                
        int grantedBefore = SecurityFontProxy.getAccessGrantedCount();
        int deniedBefore = SecurityFontProxy.getAccessDeniedCount();
        
        FontManager manager = new FontManager(25, Color.WHITE, "Warungasem.ttf", "SecurityTest");
        manager.switchToSecurityProxy(true);
        
        System.out.println("STEP 1: Access font 100 times (access GRANTED)");
        long[] accessTimes = new long[100];
        
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            BitmapFont font = manager.getFontResource().getFont();
            accessTimes[i] = System.nanoTime() - start;
        }
        
        long totalTime = 0;
        for (long t : accessTimes) {
            totalTime += t;
        }
        double avgAccessTime = totalTime / (100.0 * 1000.0); // microseconds
        
        int grantedAfter = SecurityFontProxy.getAccessGrantedCount() - grantedBefore;
        
        System.out.println("  Average access time: " + String.format("%.2fμs", avgAccessTime));
        System.out.println("  Accesses granted: " + grantedAfter + "\n");
        
        System.out.println("STEP 2: Deny access, try 50 times");
        manager.setSecurityAccess(false);
        
        long deniedStart = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            manager.getFontResource().getFont(); // Returns null
        }
        long deniedTime = System.nanoTime() - deniedStart;
        double avgDeniedTime = deniedTime / (50.0 * 1000.0);
        
        int deniedAfter = SecurityFontProxy.getAccessDeniedCount() - deniedBefore;
        
        System.out.println("  Average denial time: " + String.format("%.2fμs", avgDeniedTime));
        System.out.println("  Accesses denied: " + deniedAfter + "\n");
        
        System.out.println("RESULTS:");
        System.out.println("  Security check overhead: ~" + String.format("%.2fμs", avgAccessTime));
        System.out.println("  Denial is fast: ~" + String.format("%.2fμs", avgDeniedTime));
        
        manager.dispose();
    }
}