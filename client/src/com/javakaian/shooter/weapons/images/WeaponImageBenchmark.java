package com.javakaian.shooter.weapons.images;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark utility to measure weapon image loading performance
 * Compares direct loading vs Flyweight pattern caching
 */
public class WeaponImageBenchmark {
    
    private static final Logger logger = Logger.getLogger(WeaponImageBenchmark.class);
    
    private static final String[] TEST_WEAPONS = {
        "assault_rifle",
        "combat_shotgun", 
        "precision_sniper",
        "pistol",
        "smg"
    };
    
    /**
     * Run complete benchmark suite
     */
    public static BenchmarkResult runBenchmark() {
        logger.info("=".repeat(70));
        logger.info("WEAPON IMAGE LOADING BENCHMARK - Flyweight Pattern");
        logger.info("=".repeat(70));
        
        BenchmarkResult result = new BenchmarkResult();
        
        // Test 1: Direct loading without caching (baseline)
        result.directLoadingResults = benchmarkDirectLoading();
        
        // Test 2: First access with Flyweight (cache miss)
        result.firstAccessResults = benchmarkFirstAccess();
        
        // Test 3: Subsequent accesses with Flyweight (cache hits)
        result.cachedAccessResults = benchmarkCachedAccess();
        
        // Calculate performance improvements
        result.calculateImprovements();
        
        // Print summary
        result.printSummary();
        
        return result;
    }
    
    /**
     * Benchmark: Direct texture loading without flyweight caching
     */
    private static TestResult benchmarkDirectLoading() {
        logger.info("\n[TEST 1] Direct Loading (No Cache) - Baseline");
        logger.info("-".repeat(70));
        
        TestResult result = new TestResult("Direct Loading");
        List<Long> loadTimes = new ArrayList<>();
        
        for (String weapon : TEST_WEAPONS) {
            long startTime = System.nanoTime();
            
            try {
                String path = "weapons/" + weapon + ".png";
                Texture texture = new Texture(Gdx.files.internal(path));
                long duration = System.nanoTime() - startTime;
                loadTimes.add(duration);
                
                logger.info(String.format("  %s: %.3f ms", weapon, duration / 1_000_000.0));
                
                texture.dispose();
            } catch (Exception e) {
                // Fallback to badlogic.jpg
                try {
                    Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));
                    long duration = System.nanoTime() - startTime;
                    loadTimes.add(duration);
                    logger.info(String.format("  %s: %.3f ms (fallback)", weapon, duration / 1_000_000.0));
                    texture.dispose();
                } catch (Exception ex) {
                    logger.error("Failed to load even fallback texture");
                }
            }
        }
        
        result.setTimes(loadTimes);
        logger.info(String.format("\nTotal time: %.3f ms | Average: %.3f ms\n", 
            result.totalTime / 1_000_000.0, result.averageTime / 1_000_000.0));
        
        return result;
    }
    
    /**
     * Benchmark: First access using Flyweight (cache miss scenario)
     */
    private static TestResult benchmarkFirstAccess() {
        logger.info("\n[TEST 2] First Access via Flyweight (Cache Miss)");
        logger.info("-".repeat(70));
        
        WeaponImageFactory factory = WeaponImageFactory.getInstance();
        factory.clearPool();
        factory.resetStats();
        
        TestResult result = new TestResult("First Access (Cache Miss)");
        List<Long> loadTimes = new ArrayList<>();
        
        for (String weapon : TEST_WEAPONS) {
            long startTime = System.nanoTime();
            
            WeaponImage image = factory.getWeaponImage(weapon);
            image.getTexture(); // Trigger actual loading
            
            long duration = System.nanoTime() - startTime;
            loadTimes.add(duration);
            
            logger.info(String.format("  %s: %.3f ms (cached in pool)", weapon, duration / 1_000_000.0));
        }
        
        result.setTimes(loadTimes);
        
        WeaponImageFactory.FlyweightStats stats = factory.getStats();
        logger.info(String.format("\nTotal time: %.3f ms | Average: %.3f ms", 
            result.totalTime / 1_000_000.0, result.averageTime / 1_000_000.0));
        logger.info(String.format("Flyweight Stats: %s\n", stats));
        
        return result;
    }
    
    /**
     * Benchmark: Subsequent accesses using Flyweight (cache hit scenario)
     */
    private static TestResult benchmarkCachedAccess() {
        logger.info("\n[TEST 3] Cached Access via Flyweight (Cache Hit)");
        logger.info("-".repeat(70));
        
        WeaponImageFactory factory = WeaponImageFactory.getInstance();
        // Pool already has weapons from Test 2
        
        TestResult result = new TestResult("Cached Access (Cache Hit)");
        List<Long> accessTimes = new ArrayList<>();
        
        // Simulate multiple weapon switches (100 times)
        int iterations = 100;
        logger.info(String.format("Simulating %d weapon switches...\n", iterations));
        
        long totalStart = System.nanoTime();
        
        for (int i = 0; i < iterations; i++) {
            String weapon = TEST_WEAPONS[i % TEST_WEAPONS.length];
            
            long startTime = System.nanoTime();
            WeaponImage image = factory.getWeaponImage(weapon);
            image.getTexture();
            long duration = System.nanoTime() - startTime;
            
            accessTimes.add(duration);
        }
        
        long totalDuration = System.nanoTime() - totalStart;
        
        result.setTimes(accessTimes);
        
        WeaponImageFactory.FlyweightStats stats = factory.getStats();
        
        logger.info(String.format("Total time for %d accesses: %.3f ms", iterations, totalDuration / 1_000_000.0));
        logger.info(String.format("Average per access: %.6f ms (%.3f μs)", 
            result.averageTime / 1_000_000.0, result.averageTime / 1_000.0));
        logger.info(String.format("Flyweight Stats: %s\n", stats));
        
        return result;
    }
    
    /**
     * Test result data structure
     */
    public static class TestResult {
        String testName;
        long totalTime;
        long averageTime;
        long minTime;
        long maxTime;
        int sampleCount;
        
        public TestResult(String testName) {
            this.testName = testName;
        }
        
        public void setTimes(List<Long> times) {
            this.sampleCount = times.size();
            this.totalTime = times.stream().mapToLong(Long::longValue).sum();
            this.averageTime = this.sampleCount > 0 ? this.totalTime / this.sampleCount : 0;
            this.minTime = times.stream().mapToLong(Long::longValue).min().orElse(0);
            this.maxTime = times.stream().mapToLong(Long::longValue).max().orElse(0);
        }
    }
    
    /**
     * Complete benchmark result with comparisons
     */
    public static class BenchmarkResult {
        TestResult directLoadingResults;
        TestResult firstAccessResults;
        TestResult cachedAccessResults;
        
        double firstAccessSpeedup;
        double cachedAccessSpeedup;
        double memorySavings;
        
        public void calculateImprovements() {
            if (directLoadingResults != null && cachedAccessResults != null) {
                cachedAccessSpeedup = (double) directLoadingResults.averageTime / cachedAccessResults.averageTime;
            }
            
            if (firstAccessResults != null && cachedAccessResults != null) {
                firstAccessSpeedup = (double) firstAccessResults.averageTime / cachedAccessResults.averageTime;
            }
            
            // Memory savings: without flyweight, each access creates new texture
            // with flyweight, textures are shared
            int uniqueWeapons = TEST_WEAPONS.length;
            memorySavings = ((100.0 - uniqueWeapons) / 100.0) * 100; // % saved in 100 accesses
        }
        
        public void printSummary() {
            logger.info("\n" + "=".repeat(70));
            logger.info("BENCHMARK SUMMARY");
            logger.info("=".repeat(70));
            
            logger.info(String.format("\nDirect Loading (Baseline):   %.3f ms average", 
                directLoadingResults.averageTime / 1_000_000.0));
            logger.info(String.format("First Access (Cache Miss):   %.3f ms average", 
                firstAccessResults.averageTime / 1_000_000.0));
            logger.info(String.format("Cached Access (Cache Hit):   %.6f ms average (%.3f μs)", 
                cachedAccessResults.averageTime / 1_000_000.0,
                cachedAccessResults.averageTime / 1_000.0));
            
            logger.info("\nPERFORMANCE IMPROVEMENTS:");
            logger.info(String.format("  Cache Hit vs Direct:       %.0fx faster", cachedAccessSpeedup));
            logger.info(String.format("  Cache Hit vs Cache Miss:   %.0fx faster", firstAccessSpeedup));
            logger.info(String.format("  Memory Savings (100 accesses): ~%.0f%% (textures shared via Flyweight)", 
                memorySavings));
            
            WeaponImageFactory.FlyweightStats stats = WeaponImageFactory.getInstance().getStats();
            logger.info("\nFLYWEIGHT PATTERN EFFICIENCY:");
            logger.info(String.format("  Total Requests:  %d", stats.totalRequests));
            logger.info(String.format("  Cache Hits:      %d (%.1f%%)", stats.cacheHits, stats.hitRatio));
            logger.info(String.format("  Cache Misses:    %d", stats.cacheMisses));
            logger.info(String.format("  Pool Size:       %d shared objects", stats.poolSize));
            
            logger.info("\n" + "=".repeat(70));
        }
    }
}
