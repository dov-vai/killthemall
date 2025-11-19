package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Implementation 1
 * <p>
 * SingleShotMechanism fires one projectile per trigger pull.
 * This is the most accurate firing mode with standard fire rate.
 */
public class SingleShotMechanism implements FiringMechanism {

    private boolean isFiring = false;

    @Override
    public int startFire(String weaponName, float damage) {
        isFiring = true;
        System.out.println("[SINGLE SHOT] " + weaponName + " fires 1 projectile (Damage: " + damage + ")");
        return 1; // Single projectile
    }

    @Override
    public void stopFire(String weaponName) {
        if (isFiring) {
            isFiring = false;
            System.out.println("[SINGLE SHOT] " + weaponName + " stopped firing");
        }
    }

    @Override
    public String getDescription() {
        return "Single Shot";
    }

    @Override
    public float getFireRateMultiplier() {
        return 1.0f; // Standard fire rate
    }
}
