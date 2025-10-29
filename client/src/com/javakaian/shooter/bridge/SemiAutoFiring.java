package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

/**
 * Concrete implementation of FiringMechanism for semi-automatic firing.
 * Fires one bullet per trigger pull with moderate fire rate.
 * 
 * This is a Concrete Implementor in the Bridge Pattern.
 */
public class SemiAutoFiring implements FiringMechanism {
    
    private int currentAmmo;
    private int maxAmmo;
    private float fireRate; // Bullets per second
    private float cooldownTimer;
    private float cooldownDuration;
    
    public SemiAutoFiring() {
        this.maxAmmo = 15;
        this.currentAmmo = maxAmmo;
        this.fireRate = 3.0f; // 3 bullets per second
        this.cooldownDuration = 1.0f / fireRate;
        this.cooldownTimer = 0;
    }
    
    @Override
    public void fire(Vector2 position, Vector2 direction, Player player) {
        if (!canFire()) {
            System.out.println("Cannot fire: ammo=" + currentAmmo + ", cooldown=" + cooldownTimer);
            return;
        }
        
        // Create single bullet
        Vector2 bulletPos = new Vector2(position);
        Vector2 bulletDir = new Vector2(direction).nor();
        
        System.out.println("SemiAuto: Firing single bullet from " + position + 
                         " in direction " + direction + " | Ammo: " + (currentAmmo - 1) + "/" + maxAmmo);
        
        // In actual implementation, this would create a Bullet object
        // and add it to the game's bullet list
        
        currentAmmo--;
        cooldownTimer = cooldownDuration;
    }
    
    @Override
    public void reload() {
        currentAmmo = maxAmmo;
        System.out.println("SemiAuto: Reloaded to " + maxAmmo + " rounds");
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
        if (cooldownTimer > 0) {
            cooldownTimer -= deltaTime;
        }
    }
}
