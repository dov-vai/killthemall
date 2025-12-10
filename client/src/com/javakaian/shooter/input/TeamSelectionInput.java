package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.states.TeamSelectionState;

/**
 * Input handler for TeamSelectionState.
 * Allows players to select RED, BLUE, or GREEN team.
 */
public class TeamSelectionInput extends InputAdapter {
    
    private final TeamSelectionState teamSelectionState;
    
    public TeamSelectionInput(TeamSelectionState teamSelectionState) {
        this.teamSelectionState = teamSelectionState;
    }
    
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.NUM_1:
                teamSelectionState.selectTeam("RED");
                return true;
            case Keys.NUM_2:
                teamSelectionState.selectTeam("BLUE");
                return true;
            case Keys.NUM_3:
                teamSelectionState.selectTeam("GREEN");
                return true;
            case Keys.SPACE:
                teamSelectionState.joinGame();
                return true;
            case Keys.ESCAPE:
                teamSelectionState.backToMenu();
                return true;
            default:
                return false;
        }
    }
}
