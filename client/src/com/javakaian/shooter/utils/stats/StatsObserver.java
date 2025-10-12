package com.javakaian.shooter.utils.stats;

import com.javakaian.shooter.utils.stats.messages.StatsMessage;

public interface StatsObserver {
    void onStatsReceived(StatsMessage message);
}
