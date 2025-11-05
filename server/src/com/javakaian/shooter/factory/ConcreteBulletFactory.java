package com.javakaian.shooter.factory;

import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.StandardBullet;
import com.javakaian.shooter.shapes.FastBullet;
import com.javakaian.shooter.shapes.HeavyBullet;


public class ConcreteBulletFactory extends BulletFactory {
    public ConcreteBulletFactory() { }

    @Override
    public Bullet createBullet(BulletType type, float x, float y, float angle, int playerId) {
        return switch (type) {
            case STANDARD -> new StandardBullet(x, y, 10, angle, playerId);
            case FAST -> new FastBullet(x, y, 8, angle, playerId);
            case HEAVY -> new HeavyBullet(x, y, 10, angle, playerId);
        };
    }
}