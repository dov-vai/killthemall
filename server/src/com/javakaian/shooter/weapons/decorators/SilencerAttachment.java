package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: adds a silencer (quieter barrel) with a small damage trade-off.
 * Increases fire rate (easier to control) and reduces bullet size (subsonic ammo).
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
    public float getFireRate() {
        // Faster fire rate for full auto
        return baseWeapon.getFireRate() + 0.3f;
    }

    @Override
    public float getBulletSize() {
        // Smaller bullets (80% size)
        float baseSize = (baseWeapon instanceof WeaponAttachment)
                ? ((WeaponAttachment) baseWeapon).getBulletSize()
                : 1.0f;
        return baseSize * 0.8f;
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
        return baseWeapon.getDescription() + String.format(" + Silencer(%s, -%.0f dmg, +0.3 fire rate, 80%% bullet size)", barrelName, damagePenalty);
    }
}
