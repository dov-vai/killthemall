package com.javakaian.shooter.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages key bindings to commands.
 * Allows for easy remapping of keys to different actions.
 * Each input handler has its own KeyBindingManager, but they all share
 * the same CommandHistory singleton for undo functionality.
 */
public class KeyBindingManager {
    private final Map<Integer, InputCommand> keyBindings;

    public KeyBindingManager() {
        this.keyBindings = new HashMap<>();
    }

    /**
     * Bind a key to a command
     *
     * @param keycode The key code (from Keys class)
     * @param command The command to execute when the key is pressed
     */
    public void bindKey(int keycode, InputCommand command) {
        keyBindings.put(keycode, command);
    }

    /**
     * Unbind a key
     *
     * @param keycode The key code to unbind
     */
    public void unbindKey(int keycode) {
        keyBindings.remove(keycode);
    }

    /**
     * Handle a key press
     *
     * @param keycode The key that was pressed
     * @return true if a command was executed, false otherwise
     */
    public boolean handleKeyPress(int keycode) {
        // Special handling for undo key (Ctrl+Z)
        if (keycode == Keys.Z && (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT))) {
            System.out.println("using undo command");
            return CommandHistory.getInstance().undo();
        }

        InputCommand command = keyBindings.get(keycode);
        if (command != null) {
            CommandHistory.getInstance().executeCommand(command);
            return true;
        }
        return false;
    }

    /**
     * Get the command bound to a key
     *
     * @param keycode The key code
     * @return The bound command, or null if none
     */
    public InputCommand getCommand(int keycode) {
        return keyBindings.get(keycode);
    }

    /**
     * Clear all key bindings
     */
    public void clearBindings() {
        keyBindings.clear();
    }

    /**
     * Clear the shared command history
     */
    public void clearHistory() {
        CommandHistory.getInstance().clear();
    }
}
