package com.javakaian.shooter.utils;

public interface GameStatsListener {
    default void onSessionStarted(int totalSessions) {}
    default void onSessionEnded(float sessionTimeSeconds, boolean bestTimeUpdated, int sessionShots, float sessionDamage, float sessionDistance) {}

    default void onShotsFiredChanged(int sessionShots, int projectedTotalShots) {}
    default void onDamageTakenChanged(float sessionDamage, float projectedTotalDamage) {}
    default void onDistanceTraveledChanged(float sessionDistance, float projectedTotalDistance) {}

    default void onDeathsChanged(int totalDeaths) {}
    default void onTotalsChanged(int totalSessions, int totalDeaths, int totalShots, float totalDamage, float totalDistance, float bestTimeSeconds) {}
    default void onTotalsReset() {}
}
