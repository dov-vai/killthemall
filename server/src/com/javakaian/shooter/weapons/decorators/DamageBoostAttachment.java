package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: boosts damage output and increases bullet size.
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
    public float getBulletSize() {
        // Bigger bullets with more damage (120% size)
        float baseSize = (baseWeapon instanceof WeaponAttachment) 
                ? ((WeaponAttachment) baseWeapon).getBulletSize() 
                : 1.0f;
        return baseSize * 1.2f;
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Damage(+%.0f, 120%% bullet size)", bonusDamage);
    }
}
