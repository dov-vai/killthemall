package com.javakaian.shooter.achievements;

import org.apache.log4j.Logger;

/**
 * Demonstration of Iterator Pattern for Achievements
 * Shows how different iterators can traverse the achievement collection in different ways
 */
public class AchievementIteratorDemo {
    
    private static final Logger logger = Logger.getLogger(AchievementIteratorDemo.class);
    
    /**
     * Run a complete demonstration of the Iterator pattern
     */
    public static void runDemo(AchievementManager manager) {
        logger.info("\n" + "=".repeat(70));
        logger.info("ACHIEVEMENT ITERATOR PATTERN DEMONSTRATION");
        logger.info("=".repeat(70));
        
        // Demo 1: Iterate all achievements
        demonstrateAllAchievements(manager);
        
        // Demo 2: Iterate unlocked achievements
        demonstrateUnlockedAchievements(manager);
        
        // Demo 3: Iterate locked achievements
        demonstrateLockedAchievements(manager);
        
        // Demo 4: Show iterator features
        demonstrateIteratorFeatures(manager);
        
        logger.info("=".repeat(70));
        logger.info("ITERATOR PATTERN BENEFITS:");
        logger.info("- Uniform way to traverse different collections");
        logger.info("- Decouples collection implementation from traversal");
        logger.info("- Multiple iterators can traverse same collection");
        logger.info("- Different traversal strategies (all, filtered, etc.)");
        logger.info("=".repeat(70) + "\n");
    }
    
    private static void demonstrateAllAchievements(AchievementManager manager) {
        logger.info("\n[DEMO 1] Iterating ALL Achievements");
        logger.info("-".repeat(70));
        
        AchievementIterator iterator = manager.createIterator();
        int count = 0;
        
        while (iterator.hasNext()) {
            Achievement achievement = iterator.next();
            boolean isUnlocked = manager.isUnlocked(achievement.getId());
            String status = isUnlocked ? "[UNLOCKED]" : "[LOCKED]";
            
            logger.info(String.format("%d. %s %s - %s", 
                ++count, 
                status,
                achievement.getTitle(), 
                achievement.getDescription()));
        }
        
        logger.info(String.format("\nTotal achievements: %d", count));
    }
    
    private static void demonstrateUnlockedAchievements(AchievementManager manager) {
        logger.info("\n[DEMO 2] Iterating UNLOCKED Achievements Only");
        logger.info("-".repeat(70));
        
        AchievementIterator iterator = manager.createUnlockedIterator();
        int count = 0;
        
        if (!iterator.hasNext()) {
            logger.info("No achievements unlocked yet!");
        } else {
            while (iterator.hasNext()) {
                Achievement achievement = iterator.next();
                logger.info(String.format("%d. ✓ %s - %s", 
                    ++count,
                    achievement.getTitle(), 
                    achievement.getDescription()));
            }
            logger.info(String.format("\nTotal unlocked: %d", count));
        }
    }
    
    private static void demonstrateLockedAchievements(AchievementManager manager) {
        logger.info("\n[DEMO 3] Iterating LOCKED Achievements Only");
        logger.info("-".repeat(70));
        
        AchievementIterator iterator = manager.createLockedIterator();
        int count = 0;
        
        if (!iterator.hasNext()) {
            logger.info("All achievements unlocked! Congratulations!");
        } else {
            while (iterator.hasNext()) {
                Achievement achievement = iterator.next();
                logger.info(String.format("%d. ✗ %s - %s", 
                    ++count,
                    achievement.getTitle(), 
                    achievement.getDescription()));
            }
            logger.info(String.format("\nTotal locked: %d", count));
        }
    }
    
    private static void demonstrateIteratorFeatures(AchievementManager manager) {
        logger.info("\n[DEMO 4] Iterator Features - Reset and Current");
        logger.info("-".repeat(70));
        
        AchievementIterator iterator = manager.createIterator();
        
        // First pass - just count
        int totalCount = 0;
        while (iterator.hasNext()) {
            iterator.next();
            totalCount++;
        }
        logger.info(String.format("First pass: Counted %d achievements", totalCount));
        logger.info(String.format("hasNext() after completion: %b", iterator.hasNext()));
        
        // Reset and iterate again
        iterator.reset();
        logger.info("\nReset iterator - starting over...");
        logger.info(String.format("hasNext() after reset: %b", iterator.hasNext()));
        
        // Show first 3 achievements using current()
        logger.info("\nShowing first 3 achievements:");
        for (int i = 0; i < 3 && iterator.hasNext(); i++) {
            Achievement achievement = iterator.next();
            Achievement current = iterator.current();
            
            logger.info(String.format("%d. %s (current() confirms: %s)", 
                i + 1,
                achievement.getTitle(),
                current != null ? current.getTitle() : "null"));
        }
    }
    
    /**
     * Quick summary of current achievement status
     */
    public static void printQuickStatus(AchievementManager manager) {
        AchievementIterator allIterator = manager.createIterator();
        AchievementIterator unlockedIterator = manager.createUnlockedIterator();
        
        int total = 0;
        int unlocked = 0;
        
        while (allIterator.hasNext()) {
            allIterator.next();
            total++;
        }
        
        while (unlockedIterator.hasNext()) {
            unlockedIterator.next();
            unlocked++;
        }
        
        logger.info("\n╔════════════════════════════════════════╗");
        logger.info(String.format("║  Achievements: %d/%d unlocked (%.0f%%)  ║", 
            unlocked, total, (unlocked * 100.0 / total)));
        logger.info("╚════════════════════════════════════════╝\n");
    }
}
