package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Implementation 2
 * <p>
 * BurstFireMechanism fires 3 projectiles in quick succession per trigger pull.
 * This provides a balance between control and firepower.
 */
public class BurstFireMechanism implements FiringMechanism {

    private static final int BURST_COUNT = 3;
    private boolean isFiring = false;

    @Override
    public int startFire(String weaponName, float damage) {
        isFiring = true;
        System.out.println("[BURST FIRE] " + weaponName + " fires " + BURST_COUNT +
                " projectiles in burst (Damage: " + damage + " per shot)");
        return BURST_COUNT; // Three projectiles in burst
    }

    @Override
    public void stopFire(String weaponName) {
        if (isFiring) {
            isFiring = false;
            System.out.println("[BURST FIRE] " + weaponName + " burst complete");
        }
    }

    @Override
    public String getDescription() {
        return "Burst Fire (3-Round)";
    }

    @Override
    public float getFireRateMultiplier() {
        return 0.8f; // Slightly slower between bursts
    }

    public int getBurstCount() {
        return BURST_COUNT;
    }
}
