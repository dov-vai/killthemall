package com.javakaian.shooter.weapons.bridge;

/**
 * Decorator for BridgeWeapon - Adds scope attachment
 * Increases range, slightly reduces fire rate (heavier)
 */
public class ScopeDecorator extends BridgeWeapon {
    
    private final BridgeWeapon baseWeapon;
    private final String scopeName;
    private final float rangeBonus;
    
    public ScopeDecorator(BridgeWeapon baseWeapon, String scopeName, float rangeBonus) {
        super(baseWeapon.getName(), baseWeapon.getFiringMechanism());
        this.baseWeapon = baseWeapon;
        this.scopeName = scopeName;
        this.rangeBonus = rangeBonus;
        
        // Copy base stats
        this.damage = baseWeapon.getDamage();
        this.range = baseWeapon.getRange() + rangeBonus; // Bonus range
        this.baseFireRate = Math.max(0.1f, baseWeapon.getBaseFireRate() - 0.1f); // Slower
        this.ammoCapacity = baseWeapon.getAmmoCapacity();
        this.currentAmmo = baseWeapon.getCurrentAmmo();
    }
    
    @Override
    public String getWeaponType() {
        return baseWeapon.getWeaponType() + " [Scope]";
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
