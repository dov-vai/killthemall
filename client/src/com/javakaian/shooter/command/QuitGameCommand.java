package com.javakaian.shooter.command;

import com.javakaian.states.MenuState;

/**
 * Command to quit the application from MenuState
 * Not undoable - application exits
 */
public class QuitGameCommand implements InputCommand {
    private final MenuState menuState;

    public QuitGameCommand(MenuState menuState) {
        this.menuState = menuState;
    }

    @Override
    public void execute() {
        menuState.quit();
    }

    @Override
    public void undo() {
        // Cannot undo quitting
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Quit Game";
    }
}
