package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

/**
 * Concrete implementation of FiringMechanism for full-automatic firing.
 * Fires continuously while trigger is held with high fire rate.
 * Accuracy decreases over sustained fire.
 * 
 * This is a Concrete Implementor in the Bridge Pattern.
 */
public class FullAutoFiring implements FiringMechanism {
    
    private int currentAmmo;
    private int maxAmmo;
    private float fireRate; // Bullets per second
    private float cooldownTimer;
    private float cooldownDuration;
    private float accuracyPenalty; // Increases with sustained fire
    private float maxAccuracyPenalty;
    private float accuracyRecoveryRate;
    
    public FullAutoFiring() {
        this.maxAmmo = 50;
        this.currentAmmo = maxAmmo;
        this.fireRate = 10.0f; // 10 bullets per second
        this.cooldownDuration = 1.0f / fireRate;
        this.cooldownTimer = 0;
        this.accuracyPenalty = 0;
        this.maxAccuracyPenalty = 15.0f; // Max spread in degrees
        this.accuracyRecoveryRate = 5.0f; // Degrees per second
    }
    
    @Override
    public void fire(Vector2 position, Vector2 direction, Player player) {
        if (!canFire()) {
            System.out.println("Cannot fire: ammo=" + currentAmmo + ", cooldown=" + cooldownTimer);
            return;
        }
        
        Vector2 bulletPos = new Vector2(position);
        Vector2 bulletDir = new Vector2(direction).nor();
        
        // Apply accuracy penalty (random spread)
        if (accuracyPenalty > 0) {
            float randomSpread = (float) (Math.random() * 2 - 1) * accuracyPenalty;
            bulletDir.rotateDeg(randomSpread);
        }
        
        System.out.println("FullAuto: Firing bullet from " + position + 
                         " | Spread: " + String.format("%.1f", accuracyPenalty) + "° | " +
                         "Ammo: " + (currentAmmo - 1) + "/" + maxAmmo);
        
        // In actual implementation, create Bullet object here
        
        currentAmmo--;
        cooldownTimer = cooldownDuration;
        
        // Increase accuracy penalty for sustained fire
        accuracyPenalty = Math.min(accuracyPenalty + 1.0f, maxAccuracyPenalty);
    }
    
    @Override
    public void reload() {
        currentAmmo = maxAmmo;
        accuracyPenalty = 0; // Reset accuracy when reloading
        System.out.println("FullAuto: Reloaded to " + maxAmmo + " rounds (accuracy reset)");
    }
    
    @Override
    public boolean canFire() {
        return currentAmmo > 0 && cooldownTimer <= 0;
    }
    
    @Override
    public float getFireRate() {
        return fireRate;
    }
    
    @Override
    public int getAmmoCount() {
        return currentAmmo;
    }
    
    @Override
    public void update(float deltaTime) {
        // Update cooldown
        if (cooldownTimer > 0) {
            cooldownTimer -= deltaTime;
        }
        
        // Recover accuracy when not firing
        if (cooldownTimer <= 0 && accuracyPenalty > 0) {
            accuracyPenalty = Math.max(0, accuracyPenalty - accuracyRecoveryRate * deltaTime);
        }
    }
}
