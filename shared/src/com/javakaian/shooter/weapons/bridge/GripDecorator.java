package com.javakaian.shooter.weapons.bridge;

/**
 * Decorator for BridgeWeapon - Grip attachment
 * Significantly increases fire rate (much faster full auto)
 */
public class GripDecorator extends BridgeWeapon {
    
    private final BridgeWeapon baseWeapon;
    private final String gripName;
    private final float fireRateBonus;
    
    public GripDecorator(BridgeWeapon baseWeapon, String gripName, float fireRateBonus) {
        super(baseWeapon.getName(), baseWeapon.getFiringMechanism());
        this.baseWeapon = baseWeapon;
        this.gripName = gripName;
        this.fireRateBonus = fireRateBonus;
        
        // Copy base stats
        this.damage = baseWeapon.getDamage();
        this.range = baseWeapon.getRange();
        this.baseFireRate = baseWeapon.getBaseFireRate(); // Will apply conditionally
        this.ammoCapacity = baseWeapon.getAmmoCapacity();
        this.currentAmmo = baseWeapon.getCurrentAmmo();
    }
    
    @Override
    public float getEffectiveFireRate() {
        // Only apply fire rate bonus in Full Auto mode
        if (firingMechanism instanceof FullAutoMechanism) {
            return (baseWeapon.getBaseFireRate() + fireRateBonus) * firingMechanism.getFireRateMultiplier();
        }
        return baseWeapon.getEffectiveFireRate();
    }
    
    @Override
    public String getWeaponType() {
        return baseWeapon.getWeaponType() + " [Grip]";
    }
    
    @Override
    public void decrementAmmo(int amount) {
        super.decrementAmmo(amount);
        baseWeapon.decrementAmmo(amount);
    }
    
    @Override
    public void reload() {
        super.reload();
        baseWeapon.reload();
    }
}
