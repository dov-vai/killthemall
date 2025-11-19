package com.javakaian.shooter.command;

import com.javakaian.states.StatsState;

/**
 * Command to reset stats in StatsState
 * Not undoable - permanently resets statistics
 */
public class ResetStatsCommand implements InputCommand {
    private final StatsState statsState;

    public ResetStatsCommand(StatsState statsState) {
        this.statsState = statsState;
    }

    @Override
    public void execute() {
        statsState.resetStats();
    }

    @Override
    public void undo() {
        // Cannot undo reset
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Reset Statistics";
    }
}
