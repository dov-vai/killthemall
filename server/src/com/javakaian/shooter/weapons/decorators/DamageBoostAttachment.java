package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: boosts damage output.
 */
public class DamageBoostAttachment extends WeaponAttachment {

    private final float bonusDamage;

    public DamageBoostAttachment(Weapon baseWeapon, float bonusDamage) {
        super(baseWeapon);
        this.bonusDamage = bonusDamage;
    }

    @Override
    public float getDamage() {
        return Math.max(0f, baseWeapon.getDamage() + bonusDamage);
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Damage(+%.0f)", bonusDamage);
    }
}
