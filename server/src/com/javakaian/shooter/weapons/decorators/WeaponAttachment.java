package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Abstract Decorator for Weapon.
 * - Implements the same abstraction (Weapon)
 * - Holds a reference to a wrapped Weapon
 * - Delegates calls by default to enable chaining
 */
public abstract class WeaponAttachment extends Weapon {

    protected final Weapon baseWeapon; // the wrapped component

    protected WeaponAttachment(Weapon baseWeapon) {
        // Keep the base weapon name; decorators shouldn't rename the weapon
        super(baseWeapon.getName());
        this.baseWeapon = baseWeapon;
    }

    // Delegate getters to the wrapped component by default
    @Override public String getName() { return baseWeapon.getName(); }
    @Override public float getDamage() { return baseWeapon.getDamage(); }
    @Override public float getRange() { return baseWeapon.getRange(); }
    @Override public float getFireRate() { return baseWeapon.getFireRate(); }
    @Override public int getAmmoCapacity() { return baseWeapon.getAmmoCapacity(); }
    @Override public String getBarrel() { return baseWeapon.getBarrel(); }
    @Override public String getScope() { return baseWeapon.getScope(); }
    @Override public String getStock() { return baseWeapon.getStock(); }
    @Override public String getMagazine() { return baseWeapon.getMagazine(); }
    @Override public String getGrip() { return baseWeapon.getGrip(); }

    // Delegate mutators too, to avoid diverging state
    @Override public void setBarrel(String barrel) { baseWeapon.setBarrel(barrel); }
    @Override public void setScope(String scope) { baseWeapon.setScope(scope); }
    @Override public void setStock(String stock) { baseWeapon.setStock(stock); }
    @Override public void setMagazine(String magazine) { baseWeapon.setMagazine(magazine); }
    @Override public void setGrip(String grip) { baseWeapon.setGrip(grip); }
    @Override public void setDamage(float damage) { baseWeapon.setDamage(damage); }
    @Override public void setRange(float range) { baseWeapon.setRange(range); }
    @Override public void setFireRate(float fireRate) { baseWeapon.setFireRate(fireRate); }
    @Override public void setAmmoCapacity(int ammoCapacity) { baseWeapon.setAmmoCapacity(ammoCapacity); }

    /**
     * Expose the wrapped weapon so systems can inspect the underlying type if needed.
     */
    public Weapon getWrapped() {
        return baseWeapon;
    }

    // Delegate behavior – concrete decorators can add before/after actions
    @Override
    public void shoot() {
        baseWeapon.shoot();
    }

    // Extendable description – append attachment-specific details in subclasses
    @Override
    public String getDescription() {
        return baseWeapon.getDescription();
    }
}
