package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.command.*;
import com.javakaian.states.AchievementsState;

public class AchievementsStateInput extends InputAdapter {
    private final AchievementsState state;
    private final KeyBindingManager keyBindingManager;

    public AchievementsStateInput(AchievementsState state) { 
        this.state = state;
        this.keyBindingManager = new KeyBindingManager();
        setupDefaultKeyBindings();
    }
    
    /**
     * Setup default key bindings for AchievementsState
     */
    private void setupDefaultKeyBindings() {
        keyBindingManager.bindKey(Keys.ESCAPE, new AchievementsToMenuCommand(state));
    }

    @Override
    public boolean keyDown(int keycode) {
        return keyBindingManager.handleKeyPress(keycode);
    }
    
    /**
     * Get the key binding manager for runtime key reconfiguration
     */
    public KeyBindingManager getKeyBindingManager() {
        return keyBindingManager;
    }
}
