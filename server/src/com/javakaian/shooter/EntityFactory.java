package com.javakaian.shooter;

import com.javakaian.shooter.shapes.GameObject;

public abstract class EntityFactory {
    
    public abstract GameObject createEnemy();
    
    public abstract GameObject createPlayer(float x, float y, int id);
    
    public abstract GameObject createBullet(float x, float y, float angle, int playerId);
}