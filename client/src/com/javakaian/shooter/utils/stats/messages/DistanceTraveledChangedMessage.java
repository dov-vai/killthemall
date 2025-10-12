package com.javakaian.shooter.utils.stats.messages;

public record DistanceTraveledChangedMessage(float sessionDistance,
                                             float projectedTotalDistance) implements StatsMessage {
}
