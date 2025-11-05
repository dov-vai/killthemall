package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: boosts damage output and increases bullet size DRAMATICALLY.
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
        // MUCH bigger bullets with more damage (300% size = 3x bigger!)
        float baseSize = (baseWeapon instanceof WeaponAttachment) 
                ? ((WeaponAttachment) baseWeapon).getBulletSize() 
                : 1.0f;
        return baseSize * 3.0f; // Triple the size for drastic visual effect!
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Damage(+%.0f, 300%% bullet size - HUGE BULLETS)", bonusDamage);
    }
}
