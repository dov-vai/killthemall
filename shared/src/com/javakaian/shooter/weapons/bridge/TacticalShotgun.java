package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Refined Abstraction 2
 * <p>
 * TacticalShotgun excels at close range with high damage.
 * Works especially well with burst fire or single shot mechanisms.
 */
public class TacticalShotgun extends BridgeWeapon {

    public TacticalShotgun(FiringMechanism firingMechanism) {
        super("Tactical Shotgun", firingMechanism);
        this.damage = 35.0f;
        this.range = 50.0f;
        this.baseFireRate = 0.8f;
        this.ammoCapacity = 8;
        this.currentAmmo = ammoCapacity;
    }

    @Override
    public String getWeaponType() {
        return "Tactical Shotgun - High damage close-range weapon";
    }
}
