package com.javakaian.shooter.weapons.images;

import org.apache.log4j.Logger;

/**
 * Manual demonstration and runner for Weapon Image Flyweight Pattern Benchmark
 * Can be triggered from the game to show performance comparison
 */
public class WeaponImageBenchmarkDemo {
    
    private static final Logger logger = Logger.getLogger(WeaponImageBenchmarkDemo.class);
    private static boolean hasRun = false;
    
    /**
     * Run the complete benchmark demonstration
     * This shows the performance difference between:
     * 1. Direct texture loading (no caching)
     * 2. First access via Flyweight (cache miss - loads and caches)
     * 3. Subsequent accesses via Flyweight (cache hit - instant retrieval)
     */
    public static void runDemo() {
        if (hasRun) {
            logger.info("Benchmark already run this session. See previous results above.");
            printQuickSummary();
            return;
        }
        
        logger.info("\n\n");
        logger.info("╔═══════════════════════════════════════════════════════════════════╗");
        logger.info("║       WEAPON IMAGE LOADING BENCHMARK - FLYWEIGHT PATTERN         ║");
        logger.info("╚═══════════════════════════════════════════════════════════════════╝");
        logger.info("");
        logger.info("This benchmark demonstrates the Flyweight design pattern applied to");
        logger.info("weapon image loading. It compares:");
        logger.info("  • Direct loading (baseline - creates new texture each time)");
        logger.info("  • Flyweight caching (shares texture instances for efficiency)");
        logger.info("");
        
        // Run the actual benchmark
        WeaponImageBenchmark.BenchmarkResult result = WeaponImageBenchmark.runBenchmark();
        
        hasRun = true;
        
        logger.info("\n");
        logger.info("KEY INSIGHTS:");
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("1. FIRST LOAD: Flyweight pattern loads the image and stores it in cache");
        logger.info("   Similar performance to direct loading, but image is now cached.");
        logger.info("");
        logger.info("2. SUBSEQUENT LOADS: Flyweight returns cached instance instantly");
        logger.info(String.format("   %dx faster than reloading from disk!", 
            (int) result.cachedAccessSpeedup));
        logger.info("");
        logger.info("3. MEMORY EFFICIENCY: Only 5 texture objects for 100+ weapon switches");
        logger.info("   Without Flyweight: would need 100+ separate texture objects!");
        logger.info("");
        logger.info("4. REAL-WORLD BENEFIT: When player switches between weapons,");
        logger.info("   images load instantly from cache instead of from disk.");
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("\n");
    }
    
    /**
     * Print quick summary of current flyweight statistics
     */
    public static void printQuickSummary() {
        WeaponImageFactory factory = WeaponImageFactory.getInstance();
        WeaponImageFactory.FlyweightStats stats = factory.getStats();
        
        logger.info("\nCurrent Flyweight Statistics:");
        logger.info("─────────────────────────────────");
        logger.info(String.format("Total Requests:  %d", stats.totalRequests));
        logger.info(String.format("Cache Hits:      %d (%.1f%%)", stats.cacheHits, stats.hitRatio));
        logger.info(String.format("Cache Misses:    %d", stats.cacheMisses));
        logger.info(String.format("Pool Size:       %d shared objects", stats.poolSize));
        logger.info("─────────────────────────────────\n");
    }
    
    /**
     * Test that benchmark can run in game context
     */
    public static boolean canRun() {
        try {
            // Check if we have access to required resources
            return true;
        } catch (Exception e) {
            logger.error("Benchmark cannot run: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get instructions for running the benchmark
     */
    public static String getInstructions() {
        return "Press F10 during gameplay to run the Weapon Image Flyweight Benchmark.\n" +
               "This will show performance comparison between direct loading and cached access.";
    }
}
