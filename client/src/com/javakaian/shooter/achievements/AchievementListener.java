package com.javakaian.shooter.achievements;

public interface AchievementListener {
    default void onAchievementUnlocked(Achievement achievement) {}
}
