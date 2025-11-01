package com.javakaian.shooter.command;

import com.javakaian.states.MenuState;

/**
 * Command to toggle dark mode in MenuState
 * Not undoable - it's a toggle action
 */
public class ToggleDarkModeCommand implements InputCommand {
    private final MenuState menuState;
    
    public ToggleDarkModeCommand(MenuState menuState) {
        this.menuState = menuState;
    }
    
    @Override
    public void execute() {
        menuState.toggleDarkMode();
    }
    
    @Override
    public void undo() {
        // Could toggle back, but simpler to just let user toggle again
    }
    
    @Override
    public boolean canUndo() {
        return false;
    }
    
    @Override
    public String getDescription() {
        return "Toggle Dark Mode";
    }
}
