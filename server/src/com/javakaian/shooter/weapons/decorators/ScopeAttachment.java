package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: adds a scope to improve range and slightly reduce fire rate (heavier).
 */
public class ScopeAttachment extends WeaponAttachment {

    private final String scopeName;
    private final float bonusRange;

    public ScopeAttachment(Weapon baseWeapon, String scopeName, float bonusRange) {
        super(baseWeapon);
        this.scopeName = scopeName;
        this.bonusRange = bonusRange;
    }

    @Override
    public float getRange() {
        return baseWeapon.getRange() + bonusRange;
    }
    
    @Override
    public float getFireRate() {
        // Slightly slower fire rate (heavier optic)
        return Math.max(0.1f, baseWeapon.getFireRate() - 0.1f);
    }
    
    @Override
    public float getBulletSize() {
        // No change to bullet size
        float baseSize = (baseWeapon instanceof WeaponAttachment) 
                ? ((WeaponAttachment) baseWeapon).getBulletSize() 
                : 1.0f;
        return baseSize;
    }

    @Override
    public String getScope() {
        return scopeName;
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Scope(%s, +%.0f range, -0.1 fire rate)", scopeName, bonusRange);
    }
}
