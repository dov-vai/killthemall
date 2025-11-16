package com.javakaian.shooter.command;

import com.javakaian.states.PlayState;

/**
 * Command to shoot in PlayState
 * This is not undoable as we can't take back a shot
 */
public class ShootCommand implements InputCommand {
    private final PlayState playState;

    public ShootCommand(PlayState playState) {
        this.playState = playState;
    }

    @Override
    public void execute() {
        playState.shoot();
    }

    @Override
    public void undo() {
        // Cannot undo a shot
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Shoot";
    }
}
