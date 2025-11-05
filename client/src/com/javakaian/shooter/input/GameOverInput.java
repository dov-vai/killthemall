package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.command.*;
import com.javakaian.states.GameOverState;

/**
 * Input handles of Game Over State using Command pattern
 *
 * @author oguz
 */
public class GameOverInput extends InputAdapter {

    private GameOverState gameOver;
    private KeyBindingManager keyBindingManager;

    public GameOverInput(GameOverState game) {
        this.gameOver = game;
        this.keyBindingManager = new KeyBindingManager();
        setupDefaultKeyBindings();
    }
    
    /**
     * Setup default key bindings for GameOverState
     */
    private void setupDefaultKeyBindings() {
        keyBindingManager.bindKey(Keys.R, new RestartGameCommand(gameOver));
        keyBindingManager.bindKey(Keys.M, new GameOverToMenuCommand(gameOver));
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
