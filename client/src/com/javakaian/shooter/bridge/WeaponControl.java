package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

/**
 * Abstract base class representing weapon control modes.
 * This is the Abstraction in the Bridge Pattern.
 * 
 * It maintains a reference to a FiringMechanism (the Implementor)
 * and delegates firing operations to it while handling control-specific
 * behavior like aiming and special actions.
 */
public abstract class WeaponControl {
    
    protected FiringMechanism firingMechanism;
    protected String controlName;
    
    /**
     * Constructor that initializes with a firing mechanism.
     * 
     * @param firingMechanism The firing mechanism to use
     */
    public WeaponControl(FiringMechanism firingMechanism) {
        this.firingMechanism = firingMechanism;
    }
    
    /**
     * Execute the firing action. This combines aiming and firing logic.
     * Each control mode implements this differently.
     * 
     * @param targetPosition The target position to aim at
     * @param player The player performing the action
     */
    public abstract void execute(Vector2 targetPosition, Player player);
    
    /**
     * Handle aiming logic specific to this control mode.
     * 
     * @param targetPosition The position to aim at
     * @param player The player aiming
     * @return The adjusted aim direction
     */
    public abstract Vector2 aim(Vector2 targetPosition, Player player);
    
    /**
     * Perform a special action specific to this control mode.
     * Examples: toggle precision mode, lock target, etc.
     */
    public abstract void specialAction();
    
    /**
     * Reload the weapon through the firing mechanism.
     */
    public void reload() {
        firingMechanism.reload();
        System.out.println("[" + controlName + "] Reloading weapon...");
    }
    
    /**
     * Update the control and firing mechanism state.
     * 
     * @param deltaTime Time since last update
     */
    public void update(float deltaTime) {
        firingMechanism.update(deltaTime);
    }
    
    /**
     * Change the firing mechanism at runtime (Bridge Pattern flexibility).
     * 
     * @param firingMechanism The new firing mechanism
     */
    public void setFiringMechanism(FiringMechanism firingMechanism) {
        this.firingMechanism = firingMechanism;
        System.out.println("[" + controlName + "] Switched to new firing mechanism: " + 
                         firingMechanism.getClass().getSimpleName());
    }
    
    /**
     * Get the current firing mechanism.
     * 
     * @return The firing mechanism
     */
    public FiringMechanism getFiringMechanism() {
        return firingMechanism;
    }
    
    /**
     * Get the control mode name.
     * 
     * @return The control name
     */
    public String getControlName() {
        return controlName;
    }
}
