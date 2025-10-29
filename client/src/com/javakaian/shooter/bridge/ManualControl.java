package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

/**
 * Concrete implementation of WeaponControl for manual control.
 * Player has full control over aiming with direct input.
 * 
 * This is a Refined Abstraction in the Bridge Pattern.
 */
public class ManualControl extends WeaponControl {
    
    private boolean precisionMode;
    private float precisionSlowFactor;
    private Vector2 currentAim;
    
    public ManualControl(FiringMechanism firingMechanism) {
        super(firingMechanism);
        this.controlName = "Manual Control";
        this.precisionMode = false;
        this.precisionSlowFactor = 0.5f;
        this.currentAim = new Vector2();
    }
    
    @Override
    public void execute(Vector2 targetPosition, Player player) {
        System.out.println("\n[" + controlName + "] Executing fire command...");
        
        // Aim at target
        Vector2 aimDirection = aim(targetPosition, player);
        
        // Fire using the current firing mechanism
        firingMechanism.fire(player.getPosition(), aimDirection, player);
    }
    
    @Override
    public Vector2 aim(Vector2 targetPosition, Player player) {
        Vector2 playerPos = player.getPosition();
        Vector2 aimDirection = new Vector2(targetPosition).sub(playerPos).nor();
        
        if (precisionMode) {
            // In precision mode, smoothly interpolate to target
            // This would normally use lerp with deltaTime, simplified here
            currentAim.set(aimDirection);
            System.out.println("[" + controlName + "] Precision aim at: " + targetPosition + 
                             " (direction: " + aimDirection + ")");
        } else {
            // Direct aim - instant response
            currentAim.set(aimDirection);
            System.out.println("[" + controlName + "] Direct aim at: " + targetPosition + 
                             " (direction: " + aimDirection + ")");
        }
        
        return currentAim;
    }
    
    @Override
    public void specialAction() {
        precisionMode = !precisionMode;
        System.out.println("[" + controlName + "] Precision mode " + 
                         (precisionMode ? "ENABLED" : "DISABLED") + 
                         " (aim speed: " + (precisionMode ? "SLOW" : "NORMAL") + ")");
    }
    
    /**
     * Check if precision mode is active.
     * 
     * @return true if in precision mode
     */
    public boolean isPrecisionMode() {
        return precisionMode;
    }
}
