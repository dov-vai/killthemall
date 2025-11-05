package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.command.*;
import com.javakaian.states.MenuState;

/**
 * Input Handles of MenuState using Command pattern
 * Allows for easy key rebinding
 *
 * @author oguz
 */
public class MenuStateInput extends InputAdapter {

    private final MenuState menuState;
    private final KeyBindingManager keyBindingManager;

    public MenuStateInput(MenuState menuState) {
        this.menuState = menuState;
        this.keyBindingManager = new KeyBindingManager();
        setupDefaultKeyBindings();
    }
    
    /**
     * Setup default key bindings for MenuState
     */
    private void setupDefaultKeyBindings() {
        keyBindingManager.bindKey(Keys.SPACE, new StartGameCommand(menuState));
        keyBindingManager.bindKey(Keys.S, new NavigateToStatsCommand(menuState));
        keyBindingManager.bindKey(Keys.A, new NavigateToAchievementsCommand(menuState));
        keyBindingManager.bindKey(Keys.Q, new QuitGameCommand(menuState));
        keyBindingManager.bindKey(Keys.RIGHT, new ToggleDarkModeCommand(menuState));
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
