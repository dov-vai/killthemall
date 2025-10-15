package com.javakaian.shooter.weapons;

public class Shotgun extends Weapon {
    public Shotgun() {
        super("Shotgun");
    }

    @Override
    public void shoot() {
        System.out.println("Shotgun fires: BOOM!");
    }

    @Override
    public String getDescription() {
        return String.format("%s with %s barrel, %s scope, %s stock, %s magazine, %s grip. " +
                "Damage: %.1f, Range: %.1f, Fire Rate: %.1f, Ammo: %d",
                name, barrel, scope, stock, magazine, grip, damage, range, fireRate, ammoCapacity);
    }
}