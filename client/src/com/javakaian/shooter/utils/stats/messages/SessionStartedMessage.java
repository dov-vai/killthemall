package com.javakaian.shooter.utils.stats.messages;

public record SessionStartedMessage(int totalSessions) implements StatsMessage {
}
