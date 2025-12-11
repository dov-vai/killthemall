package com.javakaian.shooter.achievements;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Iterator Pattern - Filtered Iterator
 * Iterates over achievements that match a filter criterion
 * This demonstrates the power of the Iterator pattern - different traversal strategies
 */
public class FilteredAchievementIterator implements AchievementIterator {
    
    private final List<Achievement> filteredAchievements;
    private int currentPosition = 0;
    
    /**
     * Create a filtered iterator
     * @param achievements All achievements
     * @param filter Predicate to filter achievements
     */
    public FilteredAchievementIterator(List<Achievement> achievements, Predicate<Achievement> filter) {
        this.filteredAchievements = new ArrayList<>();
        for (Achievement achievement : achievements) {
            if (filter.test(achievement)) {
                filteredAchievements.add(achievement);
            }
        }
    }
    
    @Override
    public boolean hasNext() {
        return currentPosition < filteredAchievements.size();
    }
    
    @Override
    public Achievement next() {
        if (!hasNext()) {
            return null;
        }
        Achievement achievement = filteredAchievements.get(currentPosition);
        currentPosition++;
        return achievement;
    }
    
    @Override
    public Achievement current() {
        if (currentPosition > 0 && currentPosition <= filteredAchievements.size()) {
            return filteredAchievements.get(currentPosition - 1);
        }
        return null;
    }
    
    @Override
    public void reset() {
        currentPosition = 0;
    }
    
    /**
     * Get count of filtered achievements
     */
    public int count() {
        return filteredAchievements.size();
    }
}
