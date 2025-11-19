package com.javakaian.shooter.builder;

import com.javakaian.shooter.weapons.Rifle;
import com.javakaian.shooter.weapons.Shotgun;
import com.javakaian.shooter.weapons.Sniper;
import com.javakaian.shooter.weapons.Weapon;

public class WeaponDirector {

    public Weapon createAssaultRifle() {
        Rifle rifle = new Rifle();
        RifleBuilder builder = new RifleBuilder();

        return builder.start(rifle)
                .addBarrel()
                .addScope()
                .addStock()
                .addMagazine()
                .addGrip()
                .build();
    }

    public Weapon createCombatShotgun() {
        Shotgun shotgun = new Shotgun();
        ShotgunBuilder builder = new ShotgunBuilder();

        return builder.start(shotgun)
                .addBarrel()
                .addStock()
                .addMagazine()
                .addGrip()
                .build();
    }

    public Weapon createPrecisionSniper() {
        Sniper sniper = new Sniper();
        SniperBuilder builder = new SniperBuilder();

        return builder.start(sniper)
                .addBarrel()
                .addScope()
                .addStock()
                .addMagazine()
                .addGrip()
                .build();
    }
}