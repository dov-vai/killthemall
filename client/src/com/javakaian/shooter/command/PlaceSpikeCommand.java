package com.javakaian.shooter.command;

import com.javakaian.states.PlayState;

/**
 * Command to place a spike in PlayState
 * This is not directly undoable here (server handles spike undo separately)
 */
public class PlaceSpikeCommand implements InputCommand {
    private final PlayState playState;

    public PlaceSpikeCommand(PlayState playState) {
        this.playState = playState;
    }

    @Override
    public void execute() {
        playState.placeSpike();
    }

    @Override
    public void undo() {
        // Spike undo is handled separately on the server side
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Place Spike";
    }
}
