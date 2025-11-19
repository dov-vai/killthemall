package com.javakaian.shooter.weapons;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.factory.BulletType;
import com.javakaian.shooter.shapes.Player;

public class Shotgun extends Weapon {
    public Shotgun() {
        super("Shotgun");
    }

    @Override
    public int getProjectileCount() {
        return 8;
    }

    @Override
    public float getSpreadAngleInRadians() {
        return (float) Math.toRadians(20.0);
    }

    @Override
    public void createProjectile(ServerWorld world, Player owner, float angleRad) {
        world.createBullet(BulletType.HEAVY, owner, angleRad);
    }

    @Override
    protected void applyEffects() {
        System.out.println("BOOM! Shotgun recoil effect applied.");
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