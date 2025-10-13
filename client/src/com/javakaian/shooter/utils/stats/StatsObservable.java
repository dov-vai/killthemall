package com.javakaian.shooter.utils.stats;

import com.javakaian.shooter.utils.stats.messages.StatsMessage;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class StatsObservable {
    protected final List<StatsObserver> listeners = new ArrayList<>();

    private final Logger logger = Logger.getLogger(StatsObservable.class);

    public void addListener(StatsObserver listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(StatsObserver listener) {
        listeners.remove(listener);
    }

    protected void notify(StatsMessage message) {
        if (message == null) return;
        for (StatsObserver listener : listeners) {
            try {
                listener.onStatsReceived(message);
            } catch (Exception e) {
                logger.error("Error notifying listener: " + e.getMessage(), e);
            }
        }
    }
}
