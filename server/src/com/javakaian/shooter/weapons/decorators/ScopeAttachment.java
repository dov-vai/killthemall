package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: adds a scope to improve range.
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
    public String getScope() {
        return scopeName;
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Scope(%s, +%.0f range)", scopeName, bonusRange);
    }
}
