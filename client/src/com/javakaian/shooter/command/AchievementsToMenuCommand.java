package com.javakaian.shooter.command;

import com.javakaian.states.AchievementsState;

/**
 * Command to return to menu from AchievementsState
 * Not undoable - navigates to menu
 */
public class AchievementsToMenuCommand implements InputCommand {
    private final AchievementsState achievementsState;

    public AchievementsToMenuCommand(AchievementsState achievementsState) {
        this.achievementsState = achievementsState;
    }

    @Override
    public void execute() {
        achievementsState.backToMenu();
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
