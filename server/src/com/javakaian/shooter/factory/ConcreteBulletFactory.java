package com.javakaian.shooter.factory;

import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.StandardBullet;
import com.javakaian.shooter.shapes.FastBullet;
import com.javakaian.shooter.shapes.HeavyBullet;


public class ConcreteBulletFactory extends BulletFactory {
    public ConcreteBulletFactory() { }

    @Override
    public Bullet createBullet(BulletType type, float x, float y, float angle, int playerId) {
        return createBullet(type, x, y, angle, playerId, 1.0f);
    }
    
    @Override
    public Bullet createBullet(BulletType type, float x, float y, float angle, int playerId, float sizeMultiplier) {
        // Apply size multiplier to base sizes - make it VERY visible (multiply by 3 for drastic effect)
        float effectiveMultiplier = sizeMultiplier * 3.0f; // Make changes 3x more visible!
        
        return switch (type) {
            case STANDARD -> new StandardBullet(x, y, 10 * effectiveMultiplier, angle, playerId);
            case FAST -> new FastBullet(x, y, 8 * effectiveMultiplier, angle, playerId);
            case HEAVY -> new HeavyBullet(x, y, 10 * effectiveMultiplier, angle, playerId);
        };
    }
}