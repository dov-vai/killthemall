package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: adds a grip for better handling (much higher fire rate for full auto).
 */
public class GripAttachment extends WeaponAttachment {

    private final String gripName;
    private final float bonusFireRate;

    public GripAttachment(Weapon baseWeapon, String gripName, float bonusFireRate) {
        super(baseWeapon);
        this.gripName = gripName;
        this.bonusFireRate = Math.max(0f, bonusFireRate);
    }

    @Override
    public float getFireRate() {
        // Much faster fire rate for full auto
        return baseWeapon.getFireRate() + bonusFireRate;
    }

    @Override
    public float getBulletSize() {
        // No change to bullet size, pass through
        float baseSize = (baseWeapon instanceof WeaponAttachment)
                ? ((WeaponAttachment) baseWeapon).getBulletSize()
                : 1.0f;
        return baseSize;
    }

    @Override
    public String getGrip() {
        return gripName;
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Grip(%s, +%.1f fire rate - FULL AUTO BOOST)", gripName, bonusFireRate);
    }
}
