package com.javakaian.shooter.utils.stats.messages;

public record TotalsChangedMessage(int totalSessions, int totalDeaths, int totalShots, float totalDamage,
                                   float totalDistance, float bestTimeSeconds) implements StatsMessage {
}
