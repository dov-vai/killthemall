package com.javakaian.shooter.utils.stats.messages;

public record ShotsFiredChangedMessage(int sessionShots, int projectedTotalShots) implements StatsMessage {
}
