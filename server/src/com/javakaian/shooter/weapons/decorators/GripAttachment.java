package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: adds a grip for better handling (higher fire rate).
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
        return baseWeapon.getFireRate() + bonusFireRate;
    }

    @Override
    public String getGrip() {
        return gripName;
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Grip(%s, +%.1f fire)", gripName, bonusFireRate);
    }
}
