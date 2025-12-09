package com.javakaian.shooter.command;

import com.javakaian.shooter.utils.stats.GameStats;
import com.javakaian.states.PlayState;
import com.javakaian.states.State.StateEnum;

/**
 * Command to return to menu from PlayState
 * This is undoable - we can return to the game
 */
public class ReturnToMenuCommand implements InputCommand {
    private final PlayState playState;

    public ReturnToMenuCommand(PlayState playState) {
        this.playState = playState;
    }

    @Override
    public void execute() {
        playState.logout();
        GameStats.getInstance().endSession();
        playState.getSc().setState(StateEnum.MENU_STATE);
    }

    @Override
    public void undo() {
        System.out.println("seeintg to play state");
        playState.getSc().setState(StateEnum.PLAY_STATE);
        // Restart the session since we ended it
        GameStats.getInstance().startSession();
    }

    @Override
    public boolean canUndo() {
        return true; // We can always go back to the game
    }

    @Override
    public String getDescription() {
        return "Return to Menu";
    }
}
