package com.javakaian.shooter.command;

import com.javakaian.states.GameOverState;
import com.javakaian.states.State.StateEnum;

/**
 * Command to return to menu from GameOverState
 * Not undoable - navigates to menu
 */
public class GameOverToMenuCommand implements InputCommand {
    private final GameOverState gameOverState;
    
    public GameOverToMenuCommand(GameOverState gameOverState) {
        this.gameOverState = gameOverState;
    }
    
    @Override
    public void execute() {
        gameOverState.getSc().setState(StateEnum.MENU_STATE);
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
        return "Return to Menu";
    }
}
