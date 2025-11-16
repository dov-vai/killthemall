package com.javakaian.shooter.command;

import com.javakaian.states.MenuState;
import com.javakaian.states.State.StateEnum;

/**
 * Command to navigate to Stats State from MenuState
 * Not undoable - user can manually navigate back
 */
public class NavigateToStatsCommand implements InputCommand {
    private final MenuState menuState;

    public NavigateToStatsCommand(MenuState menuState) {
        this.menuState = menuState;
    }

    @Override
    public void execute() {
        menuState.getSc().setState(StateEnum.STATS_STATE);
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
        return "Navigate to Stats";
    }
}
