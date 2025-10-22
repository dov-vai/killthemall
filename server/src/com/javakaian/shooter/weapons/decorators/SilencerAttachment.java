package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: adds a silencer (quieter barrel) with a small damage trade-off.
 */
public class SilencerAttachment extends WeaponAttachment {

    private final String barrelName;
    private final float damagePenalty; // e.g., -2 damage

    public SilencerAttachment(Weapon baseWeapon, String barrelName, float damagePenalty) {
        super(baseWeapon);
        this.barrelName = barrelName;
        this.damagePenalty = Math.abs(damagePenalty);
    }

    @Override
    public float getDamage() {
        return Math.max(0f, baseWeapon.getDamage() - damagePenalty);
    }

    @Override
    public String getBarrel() {
        return barrelName;
    }

    @Override
    public void shoot() {
        // Silenced shot: still delegates core firing, but could modify sound/logging
        baseWeapon.shoot();
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Silencer(%s, -%.0f dmg)", barrelName, damagePenalty);
    }
}
