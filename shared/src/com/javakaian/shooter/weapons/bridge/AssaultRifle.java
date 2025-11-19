package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Refined Abstraction 1
 * <p>
 * AssaultRifle is a versatile weapon that can use any firing mechanism.
 * It has balanced stats and medium ammo capacity.
 */
public class AssaultRifle extends BridgeWeapon {

    public AssaultRifle(FiringMechanism firingMechanism) {
        super("Assault Rifle", firingMechanism);
        this.damage = 15.0f;
        this.range = 150.0f;
        this.baseFireRate = 1.2f;
        this.ammoCapacity = 30;
        this.currentAmmo = ammoCapacity;
    }

    @Override
    public String getWeaponType() {
        return "Assault Rifle - Versatile medium-range weapon";
    }
}
