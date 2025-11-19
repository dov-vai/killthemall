package com.javakaian.shooter.command;

import com.javakaian.states.StatsState;

/**
 * Command to return to menu from StatsState
 * Not undoable - navigates to menu
 */
public class StatsToMenuCommand implements InputCommand {
    private final StatsState statsState;

    public StatsToMenuCommand(StatsState statsState) {
        this.statsState = statsState;
    }

    @Override
    public void execute() {
        statsState.backToMenu();
    }

    @Override
    public void undo() {
        // Not undoable
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Back to Menu";
    }
}
