package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.states.AchievementsState;

public class AchievementsStateInput extends InputAdapter {
    private final AchievementsState state;

    public AchievementsStateInput(AchievementsState state) { this.state = state; }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE) {
            state.backToMenu();
        }
        return true;
    }
}
