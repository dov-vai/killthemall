package com.javakaian.shooter.command;

import com.javakaian.states.GameOverState;
import com.javakaian.states.State.StateEnum;

/**
 * Command to restart the game from GameOverState
 * Not undoable - restarts the game
 */
public class RestartGameCommand implements InputCommand {
    private final GameOverState gameOverState;
    
    public RestartGameCommand(GameOverState gameOverState) {
        this.gameOverState = gameOverState;
    }
    
    @Override
    public void execute() {
        gameOverState.restart();
        gameOverState.getSc().setState(StateEnum.PLAY_STATE);
    }
    
    @Override
    public void undo() {
        // Cannot undo restart
    }
    
    @Override
    public boolean canUndo() {
        return false;
    }
    
    @Override
    public String getDescription() {
        return "Restart Game";
    }
}
