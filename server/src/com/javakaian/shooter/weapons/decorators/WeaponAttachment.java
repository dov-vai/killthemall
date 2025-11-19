package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;
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
    @Override
    public String getName() {
        return baseWeapon.getName();
    }

    @Override
    public float getDamage() {
        return baseWeapon.getDamage();
    }

    @Override
    public void setDamage(float damage) {
        baseWeapon.setDamage(damage);
    }

    @Override
    public float getRange() {
        return baseWeapon.getRange();
    }

    @Override
    public void setRange(float range) {
        baseWeapon.setRange(range);
    }

    @Override
    public float getFireRate() {
        return baseWeapon.getFireRate();
    }

    @Override
    public void setFireRate(float fireRate) {
        baseWeapon.setFireRate(fireRate);
    }

    @Override
    public int getAmmoCapacity() {
        return baseWeapon.getAmmoCapacity();
    }

    @Override
    public void setAmmoCapacity(int ammoCapacity) {
        baseWeapon.setAmmoCapacity(ammoCapacity);
    }

    @Override
    public String getBarrel() {
        return baseWeapon.getBarrel();
    }

    // Delegate mutators too, to avoid diverging state
    @Override
    public void setBarrel(String barrel) {
        baseWeapon.setBarrel(barrel);
    }

    @Override
    public String getScope() {
        return baseWeapon.getScope();
    }

    @Override
    public void setScope(String scope) {
        baseWeapon.setScope(scope);
    }

    @Override
    public String getStock() {
        return baseWeapon.getStock();
    }

    @Override
    public void setStock(String stock) {
        baseWeapon.setStock(stock);
    }

    @Override
    public String getMagazine() {
        return baseWeapon.getMagazine();
    }

    @Override
    public void setMagazine(String magazine) {
        baseWeapon.setMagazine(magazine);
    }

    @Override
    public String getGrip() {
        return baseWeapon.getGrip();
    }

    @Override
    public void setGrip(String grip) {
        baseWeapon.setGrip(grip);
    }

    /**
     * Expose the wrapped weapon so systems can inspect the underlying type if
     * needed.
     */
    public Weapon getWrapped() {
        return baseWeapon;
    }

    /**
     * Get bullet size multiplier (default 1.0, decorators can override)
     */
    public float getBulletSize() {
        return 1.0f;
    }

    // Delegate behavior – concrete decorators can add before/after actions
    @Override
    public void shoot() {
        baseWeapon.shoot();
    }

    // Delegate Template Method hooks to the wrapped weapon by default
    @Override
    public int getProjectileCount() {
        return baseWeapon.getProjectileCount();
    }

    @Override
    public float getSpreadAngleInRadians() {
        return baseWeapon.getSpreadAngleInRadians();
    }

    @Override
    public void createProjectile(ServerWorld world, Player owner, float angleRad) {
        baseWeapon.createProjectile(world, owner, angleRad);
    }

    // Extendable description – append attachment-specific details in subclasses
    @Override
    public String getDescription() {
        return baseWeapon.getDescription();
    }
}
