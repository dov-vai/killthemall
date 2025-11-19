package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: increases ammo capacity with an extended magazine.
 * Slightly reduces fire rate due to extra weight.
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
        // Slightly slower fire rate due to heavier magazine
        return Math.max(0.1f, baseWeapon.getFireRate() - 0.05f);
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
        return baseWeapon.getDescription() + String.format(" + Mag(+%d ammo, -0.05 fire rate)", extraAmmo);
    }
}
