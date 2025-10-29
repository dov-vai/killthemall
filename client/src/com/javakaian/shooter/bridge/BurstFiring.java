package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

/**
 * Concrete implementation of FiringMechanism for burst-fire mode.
 * Fires a burst of bullets (default 3) per trigger pull.
 * 
 * This is a Concrete Implementor in the Bridge Pattern.
 */
public class BurstFiring implements FiringMechanism {
    
    private int currentAmmo;
    private int maxAmmo;
    private int burstSize;
    private float fireRate; // Bursts per second
    private float cooldownTimer;
    private float cooldownDuration;
    private float burstSpread; // Angular spread between burst bullets
    
    public BurstFiring() {
        this.maxAmmo = 30;
        this.currentAmmo = maxAmmo;
        this.burstSize = 3;
        this.fireRate = 2.0f; // 2 bursts per second
        this.cooldownDuration = 1.0f / fireRate;
        this.cooldownTimer = 0;
        this.burstSpread = 5.0f; // degrees
    }
    
    @Override
    public void fire(Vector2 position, Vector2 direction, Player player) {
        if (!canFire()) {
            System.out.println("Cannot fire: ammo=" + currentAmmo + ", cooldown=" + cooldownTimer);
            return;
        }
        
        int bulletsToFire = Math.min(burstSize, currentAmmo);
        System.out.println("BurstFire: Firing " + bulletsToFire + "-round burst from " + 
                         position + " | Ammo: " + (currentAmmo - bulletsToFire) + "/" + maxAmmo);
        
        // Fire burst of bullets with slight spread
        for (int i = 0; i < bulletsToFire; i++) {
            Vector2 bulletPos = new Vector2(position);
            Vector2 bulletDir = new Vector2(direction).nor();
            
            // Add spread to each bullet in burst
            float spreadOffset = (i - (bulletsToFire - 1) / 2.0f) * burstSpread;
            bulletDir.rotateDeg(spreadOffset);
            
            System.out.println("  Bullet " + (i + 1) + " fired at angle offset: " + spreadOffset + "°");
            
            // In actual implementation, create Bullet objects here
            currentAmmo--;
        }
        
        cooldownTimer = cooldownDuration;
    }
    
    @Override
    public void reload() {
        currentAmmo = maxAmmo;
        System.out.println("BurstFire: Reloaded to " + maxAmmo + " rounds");
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
    
    /**
     * Set the number of bullets per burst.
     * 
     * @param burstSize The number of bullets to fire per burst
     */
    public void setBurstSize(int burstSize) {
        this.burstSize = burstSize;
        System.out.println("BurstFire: Burst size set to " + burstSize);
    }
}
