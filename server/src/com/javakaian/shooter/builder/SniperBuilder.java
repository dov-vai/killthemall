package com.javakaian.shooter.builder;

import com.javakaian.shooter.weapons.Sniper;
import com.javakaian.shooter.weapons.Weapon;

public class SniperBuilder implements IBuilder {
    private Sniper sniper = null;

    @Override
    public IBuilder addBarrel() {
        sniper.setBarrel("Long Precision Barrel");
        sniper.setDamage(sniper.getDamage() + 30.0f);
        sniper.setRange(sniper.getRange() + 100.0f);
        return this;
    }

    @Override
    public IBuilder addScope() {
        sniper.setScope("High-Power Scope");
        sniper.setRange(sniper.getRange() + 50.0f);
        return this;
    }

    @Override
    public IBuilder addStock() {
        sniper.setStock("Bipod Stock");
        sniper.setFireRate(sniper.getFireRate() - 0.5f);
        return this;
    }

    @Override
    public IBuilder addMagazine() {
        sniper.setMagazine("Precision Magazine");
        sniper.setAmmoCapacity(sniper.getAmmoCapacity() - 5);
        return this;
    }

    @Override
    public IBuilder addGrip() {
        sniper.setGrip("Precision Grip");
        return this;
    }

    @Override
    public Weapon build() {
        return sniper;
    }

    @Override
    public IBuilder start(Weapon weapon) {
        this.sniper = (Sniper) weapon;
        return this;
    }
}