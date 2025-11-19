package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Implementation 3
 * <p>
 * FullAutoMechanism continuously fires projectiles while trigger is held.
 * This provides maximum firepower but reduced accuracy.
 */
public class FullAutoMechanism implements FiringMechanism {

    private boolean isFiring = false;
    private int projectilesFired = 0;

    @Override
    public int startFire(String weaponName, float damage) {
        if (!isFiring) {
            isFiring = true;
            projectilesFired = 0;
            System.out.println("[FULL AUTO] " + weaponName + " started continuous fire (Damage: " + damage + " per shot)");
        }
        projectilesFired++;
        return 1; // One projectile per fire call, but continuous
    }

    @Override
    public void stopFire(String weaponName) {
        if (isFiring) {
            isFiring = false;
            System.out.println("[FULL AUTO] " + weaponName + " stopped firing (Total projectiles: " + projectilesFired + ")");
            projectilesFired = 0;
        }
    }

    @Override
    public String getDescription() {
        return "Full Auto";
    }

    @Override
    public float getFireRateMultiplier() {
        return 1.5f; // Faster fire rate for full auto
    }

    public boolean isFiring() {
        return isFiring;
    }
}
