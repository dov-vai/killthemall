package com.javakaian.shooter.builder;

import com.javakaian.shooter.weapons.Rifle;
import com.javakaian.shooter.weapons.Weapon;

public class RifleBuilder implements IBuilder {
    private Rifle rifle = null;

    @Override
    public IBuilder addBarrel() {
        rifle.setBarrel("Long Rifle Barrel");
        rifle.setDamage(rifle.getDamage() + 15.0f);
        rifle.setRange(rifle.getRange() + 50.0f);
        return this;
    }

    @Override
    public IBuilder addScope() {
        rifle.setScope("Red Dot Sight");
        rifle.setRange(rifle.getRange() + 25.0f);
        return this;
    }

    @Override
    public IBuilder addStock() {
        rifle.setStock("Tactical Stock");
        rifle.setFireRate(rifle.getFireRate() + 0.2f);
        return this;
    }

    @Override
    public IBuilder addMagazine() {
        rifle.setMagazine("Extended Magazine");
        rifle.setAmmoCapacity(rifle.getAmmoCapacity() + 10);
        return this;
    }

    @Override
    public IBuilder addGrip() {
        rifle.setGrip("Ergonomic Grip");
        rifle.setFireRate(rifle.getFireRate() + 0.1f);
        return this;
    }

    @Override
    public Weapon build() {
        return rifle;
    }

    @Override
    public IBuilder start(Weapon weapon) {
        this.rifle = (Rifle) weapon;
        return this;
    }
}