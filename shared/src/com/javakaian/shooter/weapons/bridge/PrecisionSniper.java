package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Refined Abstraction 3
 * <p>
 * PrecisionSniper is designed for long-range engagements.
 * Works best with single shot or charged shot mechanisms.
 */
public class PrecisionSniper extends BridgeWeapon {

    public PrecisionSniper(FiringMechanism firingMechanism) {
        super("Precision Sniper", firingMechanism);
        this.damage = 80.0f;
        this.range = 500.0f;
        this.baseFireRate = 0.4f;
        this.ammoCapacity = 5;
        this.currentAmmo = ammoCapacity;
    }

    @Override
    public String getWeaponType() {
        return "Precision Sniper - Extreme range high-damage weapon";
    }
}
