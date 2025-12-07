package com.javakaian.shooter.command;

import com.javakaian.shooter.ThemeFactory.ThemeFactory;
import com.javakaian.states.MenuState;
import com.javakaian.states.PlayState;
import com.javakaian.states.State.StateEnum;

/**
 * Command to start the game from MenuState
 * Not undoable as we don't want to go back to menu automatically
 */
public class StartGameCommand implements InputCommand {
    private final MenuState menuState;

    public StartGameCommand(MenuState menuState) {
        this.menuState = menuState;
    }

    @Override
    public void execute() {
        // Go to team selection state first
        menuState.getSc().setState(StateEnum.TEAM_SELECTION_STATE);
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
        return "Start Game";
    }
}
