package com.javakaian.shooter.weapons;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.factory.BulletType;
import com.javakaian.shooter.shapes.Player;

public class Rifle extends Weapon {
    public Rifle() {
        super("Rifle");
    }

    @Override
    public int getProjectileCount() {
        return 1;
    }

    @Override
    public float getSpreadAngleInRadians() {
        return (float) Math.toRadians(2.0);
    }

    @Override
    public void createProjectile(ServerWorld world, Player owner, float angleRad) {
        world.createBullet(BulletType.STANDARD, owner, angleRad);
    }

    @Override
    public void shoot() {
        System.out.println("Rifle fires: BANG!");
    }

    @Override
    public String getDescription() {
        return String.format("%s with %s barrel, %s scope, %s stock, %s magazine, %s grip. " +
                "Damage: %.1f, Range: %.1f, Fire Rate: %.1f, Ammo: %d",
                name, barrel, scope, stock, magazine, grip, damage, range, fireRate, ammoCapacity);
    }
}