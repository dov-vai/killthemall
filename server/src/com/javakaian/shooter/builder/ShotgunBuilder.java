package com.javakaian.shooter.builder;

import com.javakaian.shooter.weapons.Shotgun;
import com.javakaian.shooter.weapons.Weapon;

public class ShotgunBuilder implements IBuilder {
    private Shotgun shotgun = null;

    @Override
    public IBuilder addBarrel() {
        shotgun.setBarrel("Short Shotgun Barrel");
        shotgun.setDamage(shotgun.getDamage() + 25.0f);
        shotgun.setRange(shotgun.getRange() - 20.0f);
        return this;
    }

    @Override
    public IBuilder addScope() {
        shotgun.setScope("No Scope");
        return this;
    }

    @Override
    public IBuilder addStock() {
        shotgun.setStock("Pistol Grip");
        shotgun.setFireRate(shotgun.getFireRate() - 0.3f);
        return this;
    }

    @Override
    public IBuilder addMagazine() {
        shotgun.setMagazine("Drum Magazine");
        shotgun.setAmmoCapacity(shotgun.getAmmoCapacity() + 5);
        return this;
    }

    @Override
    public IBuilder addGrip() {
        shotgun.setGrip("Foregrip");
        shotgun.setFireRate(shotgun.getFireRate() + 0.2f);
        return this;
    }

    @Override
    public Weapon build() {
        return shotgun;
    }

    @Override
    public IBuilder start(Weapon weapon) {
        this.shotgun = (Shotgun) weapon;
        return this;
    }
}