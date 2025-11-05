package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: increases ammo capacity with an extended magazine.
 * Significantly reduces fire rate due to extra weight.
 */
public class ExtendedMagazineAttachment extends WeaponAttachment {

    private final int extraAmmo;

    public ExtendedMagazineAttachment(Weapon baseWeapon, int extraAmmo) {
        super(baseWeapon);
        this.extraAmmo = Math.max(0, extraAmmo);
    }

    @Override
    public int getAmmoCapacity() {
        return baseWeapon.getAmmoCapacity() + extraAmmo;
    }
    
    @Override
    public float getFireRate() {
        // Much slower fire rate due to heavier magazine (divide by 2 for drastic effect)
        return Math.max(0.1f, baseWeapon.getFireRate() / 2.0f);
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
    public String getMagazine() {
        return "Extended Magazine";
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Mag(+%d ammo, -50%% fire rate - HEAVY)", extraAmmo);
    }
}
