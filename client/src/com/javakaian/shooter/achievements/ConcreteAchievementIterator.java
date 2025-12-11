package com.javakaian.shooter.achievements;

import java.util.ArrayList;
import java.util.List;

/**
 * Iterator Pattern - Concrete Iterator
 * Implements iteration over Achievement collections with optional filtering
 */
public class ConcreteAchievementIterator implements AchievementIterator {
    
    private final List<Achievement> achievements;
    private int currentPosition = 0;
    
    public ConcreteAchievementIterator(List<Achievement> achievements) {
        this.achievements = new ArrayList<>(achievements);
    }
    
    @Override
    public boolean hasNext() {
        return currentPosition < achievements.size();
    }
    
    @Override
    public Achievement next() {
        if (!hasNext()) {
            return null;
        }
        Achievement achievement = achievements.get(currentPosition);
        currentPosition++;
        return achievement;
    }
    
    @Override
    public Achievement current() {
        if (currentPosition > 0 && currentPosition <= achievements.size()) {
            return achievements.get(currentPosition - 1);
        }
        return null;
    }
    
    @Override
    public void reset() {
        currentPosition = 0;
    }
    
    /**
     * Get total count of achievements in this iterator
     */
    public int count() {
        return achievements.size();
    }
}
