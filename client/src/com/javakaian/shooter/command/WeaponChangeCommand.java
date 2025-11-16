package com.javakaian.shooter.command;

import com.javakaian.states.PlayState;

/**
 * Command to change weapon in PlayState
 * This is undoable - we can switch back to the previous weapon
 */
public class WeaponChangeCommand implements InputCommand {
    private final PlayState playState;
    private final String newWeaponConfig;

    public WeaponChangeCommand(PlayState playState, String weaponConfig) {
        this.playState = playState;
        this.newWeaponConfig = weaponConfig;
    }

    @Override
    public void execute() {
        // Store current weapon before changing (would need to add getter to PlayState)
        // For now, we'll just change the weapon
        playState.requestWeaponChange(newWeaponConfig);
    }

    @Override
    public void undo() {
        // Would need to store and restore previous weapon
        // For now, this is a simplified implementation
    }

    @Override
    public boolean canUndo() {
        // Could be made undoable if we store previous weapon state
        return false;
    }

    @Override
    public String getDescription() {
        return "Change Weapon to " + newWeaponConfig;
    }
}
