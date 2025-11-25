package com.javakaian.shooter.shapes.flyweight;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.shooter.shapes.PowerUp.PowerUpType;

/**
 * UnsharedConcreteFlyweight stores all state (both intrinsic and extrinsic).
 * This is used when we want to maintain individual PowerUp instances with
 * their own complete state, but still participate in the flyweight pattern.
 * 
 * In this implementation, it stores all states including position and size,
 * which are typically extrinsic.
 */
public class UnsharedConcreteFlyweight implements Flyweight {
    
    private PowerUpType allStates;
    private float x;
    private float y;
    private float size;
    
    public UnsharedConcreteFlyweight(PowerUpType type, float x, float y, float size) {
        this.allStates = type;
        this.x = x;
        this.y = y;
        this.size = size;
    }
    
    @Override
    public void operation(ShapeRenderer sr, float x, float y, float size) {
        // UnsharedConcreteFlyweight uses its own stored state,
        // ignoring the extrinsic parameters passed in
        ConcreteFlyweight flyweight = new ConcreteFlyweight(this.allStates);
        flyweight.operation(sr, this.x, this.y, this.size);
    }
    
    public PowerUpType getAllStates() {
        return allStates;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getSize() {
        return size;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
