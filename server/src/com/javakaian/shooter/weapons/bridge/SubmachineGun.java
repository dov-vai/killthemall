package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Refined Abstraction 4
 * <p>
 * SubmachineGun is optimized for close to medium range rapid fire.
 * Excels with full auto or burst fire mechanisms.
 */
public class SubmachineGun extends BridgeWeapon {

    public SubmachineGun(FiringMechanism firingMechanism) {
        super("Submachine Gun", firingMechanism);
        this.damage = 8.0f;
        this.range = 80.0f;
        this.baseFireRate = 1.8f;
        this.ammoCapacity = 40;
        this.currentAmmo = ammoCapacity;
    }

    @Override
    public String getWeaponType() {
        return "Submachine Gun - High fire rate close-range weapon";
    }
}
