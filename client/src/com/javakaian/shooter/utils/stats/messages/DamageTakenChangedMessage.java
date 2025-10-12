package com.javakaian.shooter.utils.stats.messages;

public record DamageTakenChangedMessage(float sessionDamage, float projectedTotalDamage) implements StatsMessage {
}
