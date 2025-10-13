package com.javakaian.shooter.achievements;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class AchievementsObservable {

    protected final List<AchievementObserver> listeners = new ArrayList<>();

    private final Logger logger = Logger.getLogger(AchievementsObservable.class);

    public void addListener(AchievementObserver listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(AchievementObserver listener) {
        listeners.remove(listener);
    }

    protected void notify(Achievement achievement) {
        if (achievement == null) return;

        for (AchievementObserver listener : listeners) {
            try {
                listener.onAchievementUnlocked(achievement);
            } catch (Exception e) {
                logger.error("Error notifying listener: " + e.getMessage(), e);
            }
        }
    }
}
