package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Weapon;

/**
 * Concrete Decorator: increases ammo capacity with an extended magazine.
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
    public String getMagazine() {
        return "Extended Magazine";
    }

    @Override
    public String getDescription() {
        return baseWeapon.getDescription() + String.format(" + Mag(+%d ammo)", extraAmmo);
    }
}
