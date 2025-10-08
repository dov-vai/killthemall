package com.javakaian.shooter.factory;

import com.javakaian.shooter.shapes.Bullet;

public class ConcreteBulletFactory extends BulletFactory {
    public ConcreteBulletFactory() { }

    @Override
    public Bullet createBullet(BulletType type, float x, float y, float angle, int playerId) {
        return switch (type) {
            case STANDARD -> new Bullet.StandardBullet(x, y, 10, angle, playerId);
            case FAST -> new Bullet.FastBullet(x, y, 8, angle, playerId);
            case HEAVY -> new Bullet.HeavyBullet(x, y, 10, angle, playerId);
        };
    }
}