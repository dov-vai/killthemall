package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.command.*;
import com.javakaian.states.PlayState;

/**
 * Input handles of PlayState using Command pattern
 * Allows for easy key rebinding and undo functionality
 *
 * @author oguz
 */
public class PlayStateInput extends InputAdapter {

    private PlayState playState;
    private KeyBindingManager keyBindingManager;

    public PlayStateInput() {
    }

    public PlayStateInput(PlayState playState) {
        this.playState = playState;
        this.keyBindingManager = new KeyBindingManager();
        setupDefaultKeyBindings();
    }
    
    /**
     * Setup default key bindings for PlayState
     * These can be easily changed by modifying this method or by providing
     * a configuration system
     */
    private void setupDefaultKeyBindings() {
        // Basic actions
        keyBindingManager.bindKey(Keys.SPACE, new ShootCommand(playState));
        keyBindingManager.bindKey(Keys.M, new ReturnToMenuCommand(playState));
        
        // Weapon selection
        keyBindingManager.bindKey(Keys.NUM_1, new WeaponChangeCommand(playState, "assault_rifle"));
        keyBindingManager.bindKey(Keys.NUM_2, new WeaponChangeCommand(playState, "combat_shotgun"));
        keyBindingManager.bindKey(Keys.NUM_3, new WeaponChangeCommand(playState, "precision_sniper"));
        
        // Attachments toggles
        keyBindingManager.bindKey(Keys.NUM_4, new AttachmentToggleCommand(playState, "scope:4x ACOG:150"));
        keyBindingManager.bindKey(Keys.NUM_5, new AttachmentToggleCommand(playState, "mag:15"));
        keyBindingManager.bindKey(Keys.NUM_6, new AttachmentToggleCommand(playState, "grip:Tactical:0.5"));
        keyBindingManager.bindKey(Keys.NUM_7, new AttachmentToggleCommand(playState, "silencer:Silenced Barrel:2"));
        keyBindingManager.bindKey(Keys.NUM_8, new AttachmentToggleCommand(playState, "dmg:5"));
        
        // Spike actions
        keyBindingManager.bindKey(Keys.E, new PlaceSpikeCommand(playState));
        
        // Utility
        keyBindingManager.bindKey(Keys.L, new ToggleLogDisplayCommand(playState));
        
        // Note: Keys.NUM_0 for reset attachments and Keys.U for undo spike are handled separately below
        // Note: Ctrl+Z for undo is handled automatically by KeyBindingManager
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        playState.scrolled(amountY);

        return super.scrolled(amountX, amountY);
    }

    @Override
    public boolean keyDown(int keycode) {
        
        // Try to handle with command pattern first
        if (keyBindingManager.handleKeyPress(keycode)) {
            return true;
        }

        // Handle special cases that don't fit the simple command pattern
        switch (keycode) {
            case Keys.NUM_0:
                // Reset attachments, keep current base
                playState.resetAttachments();
                break;
            case Keys.U:
                // Undo spike (server-side undo, separate from client command undo)
                playState.undoSpike();
                break;
            default:
                break;
        }

        return true;
    }
    
    /**
     * Get the key binding manager for this input handler
     * Useful for reconfiguring keys at runtime
     */
    public KeyBindingManager getKeyBindingManager() {
        return keyBindingManager;
    }

}
