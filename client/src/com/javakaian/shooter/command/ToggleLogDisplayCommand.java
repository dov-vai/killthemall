package com.javakaian.shooter.command;

import com.javakaian.states.PlayState;

/**
 * Command to toggle log display in PlayState
 * This is not undoable as it's a toggle action
 */
public class ToggleLogDisplayCommand implements InputCommand {
    private final PlayState playState;
    
    public ToggleLogDisplayCommand(PlayState playState) {
        this.playState = playState;
    }
    
    @Override
    public void execute() {
        playState.toggleLogDisplay();
    }
    
    @Override
    public void undo() {
        // Could toggle back, but it's simpler to just let user toggle again
    }
    
    @Override
    public boolean canUndo() {
        return false;
    }
    
    @Override
    public String getDescription() {
        return "Toggle Log Display";
    }
}
