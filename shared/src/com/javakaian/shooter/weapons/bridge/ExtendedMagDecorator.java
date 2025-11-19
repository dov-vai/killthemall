package com.javakaian.shooter.weapons.bridge;

/**
 * Decorator for BridgeWeapon - Extended Magazine attachment
 * Increases ammo capacity, slightly reduces fire rate (heavier)
 */
public class ExtendedMagDecorator extends BridgeWeapon {

    private final BridgeWeapon baseWeapon;
    private final int extraAmmo;

    public ExtendedMagDecorator(BridgeWeapon baseWeapon, int extraAmmo) {
        super(baseWeapon.getName(), baseWeapon.getFiringMechanism());
        this.baseWeapon = baseWeapon;
        this.extraAmmo = extraAmmo;

        // Copy base stats
        this.damage = baseWeapon.getDamage();
        this.range = baseWeapon.getRange();
        this.baseFireRate = baseWeapon.getBaseFireRate(); // Will apply conditionally
        this.ammoCapacity = baseWeapon.getAmmoCapacity() + extraAmmo; // Extra ammo
        this.currentAmmo = this.ammoCapacity; // Full reload
    }

    @Override
    public float getEffectiveFireRate() {
        // Only apply fire rate penalty in Full Auto mode
        if (firingMechanism instanceof FullAutoMechanism) {
            return Math.max(0.1f, baseWeapon.getBaseFireRate() - 0.05f) * firingMechanism.getFireRateMultiplier();
        }
        return baseWeapon.getEffectiveFireRate();
    }

    @Override
    public String getWeaponType() {
        return baseWeapon.getWeaponType() + " [Extended Mag]";
    }

    @Override
    public void decrementAmmo(int amount) {
        super.decrementAmmo(amount);
    }

    @Override
    public void reload() {
        super.reload();
    }
}
