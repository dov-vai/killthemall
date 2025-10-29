package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.shapes.Enemy;
import java.util.List;

/**
 * Concrete implementation of WeaponControl with auto-aim assistance.
 * Automatically snaps aim to nearest enemy within range.
 * 
 * This is a Refined Abstraction in the Bridge Pattern.
 */
public class AutoAimControl extends WeaponControl {
    
    private float autoAimRange;
    private boolean targetLocked;
    private Enemy lockedTarget;
    private float lockDuration;
    private float lockTimer;
    
    public AutoAimControl(FiringMechanism firingMechanism) {
        super(firingMechanism);
        this.controlName = "Auto-Aim Control";
        this.autoAimRange = 300.0f;
        this.targetLocked = false;
        this.lockedTarget = null;
        this.lockDuration = 3.0f; // Lock lasts 3 seconds
        this.lockTimer = 0;
    }
    
    @Override
    public void execute(Vector2 targetPosition, Player player) {
        System.out.println("\n[" + controlName + "] Executing fire command...");
        
        // Use auto-aim to find or use locked target
        Vector2 aimDirection = aim(targetPosition, player);
        
        // Fire using the current firing mechanism
        firingMechanism.fire(player.getPosition(), aimDirection, player);
    }
    
    @Override
    public Vector2 aim(Vector2 targetPosition, Player player) {
        Vector2 playerPos = player.getPosition();
        Vector2 aimDirection;
        
        if (targetLocked && lockedTarget != null) {
            // Use locked target
            aimDirection = new Vector2(lockedTarget.getPosition()).sub(playerPos).nor();
            System.out.println("[" + controlName + "] Locked target aim: " + 
                             lockedTarget.getPosition() + " (time left: " + 
                             String.format("%.1f", lockDuration - lockTimer) + "s)");
        } else {
            // Simulate finding nearest enemy (in real implementation, search enemy list)
            Enemy nearestEnemy = findNearestEnemy(playerPos, null);
            
            if (nearestEnemy != null && playerPos.dst(nearestEnemy.getPosition()) <= autoAimRange) {
                // Snap to nearest enemy
                aimDirection = new Vector2(nearestEnemy.getPosition()).sub(playerPos).nor();
                System.out.println("[" + controlName + "] Auto-aim snap to enemy at: " + 
                                 nearestEnemy.getPosition() + 
                                 " (distance: " + String.format("%.1f", 
                                 playerPos.dst(nearestEnemy.getPosition())) + ")");
            } else {
                // No enemy in range, use manual aim
                aimDirection = new Vector2(targetPosition).sub(playerPos).nor();
                System.out.println("[" + controlName + "] No target in range, manual aim at: " + 
                                 targetPosition);
            }
        }
        
        return aimDirection;
    }
    
    @Override
    public void specialAction() {
        if (!targetLocked) {
            // Lock onto target
            targetLocked = true;
            lockTimer = 0;
            // In real implementation, would lock onto nearest enemy
            System.out.println("[" + controlName + "] TARGET LOCKED! Duration: " + lockDuration + "s");
        } else {
            // Release lock
            targetLocked = false;
            lockedTarget = null;
            lockTimer = 0;
            System.out.println("[" + controlName + "] Target lock RELEASED");
        }
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        // Update target lock timer
        if (targetLocked) {
            lockTimer += deltaTime;
            if (lockTimer >= lockDuration) {
                targetLocked = false;
                lockedTarget = null;
                lockTimer = 0;
                System.out.println("[" + controlName + "] Target lock EXPIRED");
            }
        }
    }
    
    /**
     * Find the nearest enemy to the player position.
     * In a real implementation, this would search through the game's enemy list.
     * 
     * @param playerPos The player's position
     * @param enemies List of enemies (null in this example)
     * @return The nearest enemy, or null if none found
     */
    private Enemy findNearestEnemy(Vector2 playerPos, List<Enemy> enemies) {
        // Simplified - in real implementation would search actual enemy list
        // For demonstration, return a mock enemy
        return null;
    }
    
    /**
     * Set the auto-aim range.
     * 
     * @param range The maximum range for auto-aim
     */
    public void setAutoAimRange(float range) {
        this.autoAimRange = range;
        System.out.println("[" + controlName + "] Auto-aim range set to: " + range);
    }
    
    /**
     * Check if a target is currently locked.
     * 
     * @return true if target is locked
     */
    public boolean isTargetLocked() {
        return targetLocked;
    }
}
