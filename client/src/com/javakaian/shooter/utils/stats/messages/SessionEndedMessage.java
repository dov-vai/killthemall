package com.javakaian.shooter.utils.stats.messages;

public record SessionEndedMessage(float sessionTimeSeconds, boolean bestTimeUpdated, int sessionShots,
                                  float sessionDamage, float sessionDistance) implements StatsMessage {
}
