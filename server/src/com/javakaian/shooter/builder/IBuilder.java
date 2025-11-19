package com.javakaian.shooter.builder;

import com.javakaian.shooter.weapons.Weapon;

public interface IBuilder {
    IBuilder addBarrel();

    IBuilder addScope();

    IBuilder addStock();

    IBuilder addMagazine();

    IBuilder addGrip();

    Weapon build();

    IBuilder start(Weapon weapon);
}