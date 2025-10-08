package com.javakaian.shooter.factory;

import com.javakaian.shooter.shapes.Bullet;

public abstract class BulletFactory {

    public abstract Bullet createBullet(BulletType type, float x, float y, float angle, int playerId);
    
}