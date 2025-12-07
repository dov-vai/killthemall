package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.command.*;
import com.javakaian.states.PlayState;
import com.badlogic.gdx.Input.Keys;
import com.javakaian.shooter.utils.fonts.ProxyPatternManualDemo;

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
        // Basic actions - SPACE removed, handled in keyDown/keyUp for hold functionality
        // keyBindingManager.bindKey(Keys.SPACE, new ShootCommand(playState));
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

        // Bridge Pattern - Firing mode control (using Command pattern)
        keyBindingManager.bindKey(Keys.B, new InputCommand() {
            @Override
            public void execute() {
                playState.cycleFiringMode();
            }

            @Override
            public void undo() {
            }

            @Override
            public boolean canUndo() {
                return false;
            }

            @Override
            public String getDescription() {
                return "Cycle Firing Mode";
            }
        });

        keyBindingManager.bindKey(Keys.R, new InputCommand() {
            @Override
            public void execute() {
                playState.reloadBridgeWeapon();
            }

            @Override
            public void undo() {
            }

            @Override
            public boolean canUndo() {
                return false;
            }

            @Override
            public String getDescription() {
                return "Reload Weapon";
            }
        });

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

        // Handle chat input separately
        if (playState.isChatInputActive()) {
            if (keycode == Keys.ENTER) {
                playState.sendChatMessage();
                return true;
            } else if (keycode == Keys.ESCAPE) {
                playState.toggleChatInput();
                return true;
            } else if (keycode == Keys.BACKSPACE) {
                playState.removeChatCharacter();
                return true;
            }
            // Let keyTyped handle character input
            return false;
        }

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
            case Keys.T:
                // Toggle team chat input
                playState.toggleChatInput();
                break;
            case Keys.SPACE:
                // Start shooting (for full auto and charged shot)
                playState.shoot();
                break;
            case Keys.F9:
                // Proxy pattern demo
                ProxyPatternManualDemo.runFullDemonstration();
                break;
            case Keys.F10:
                ProxyPatternManualDemo.runPerformanceReport();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Handle key release for continuous firing modes
        if (keycode == Keys.SPACE) {
            playState.stopShooting();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean keyTyped(char character) {
        // Handle character input for chat
        if (playState.isChatInputActive()) {
            if (Character.isLetterOrDigit(character) || Character.isSpaceChar(character) || 
                "!@#$%^&*()_+-=[]{}|;:',.<>?/".indexOf(character) >= 0) {
                playState.addChatCharacter(character);
                return true;
            }
        }
        return false;
    }

    /**
     * Get the key binding manager for this input handler
     * Useful for reconfiguring keys at runtime
     */
    public KeyBindingManager getKeyBindingManager() {
        return keyBindingManager;
    }

}
