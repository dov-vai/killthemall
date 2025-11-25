package com.javakaian.shooter.shapes.flyweight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.shooter.shapes.PowerUp.PowerUpType;

/**
 * ConcreteFlyweight stores intrinsic state (shared among many PowerUps).
 * The intrinsic state is the PowerUpType and its rendering behavior,
 * which is the same for all PowerUps of the same type.
 */
public class ConcreteFlyweight implements Flyweight {
    
    private final PowerUpType intrinsicState;
    
    public ConcreteFlyweight(PowerUpType intrinsicState) {
        this.intrinsicState = intrinsicState;
    }
    
    @Override
    public void operation(ShapeRenderer sr, float x, float y, float size) {
        // Set color based on intrinsic state (PowerUpType)
        switch (intrinsicState) {
            case SPEED_BOOST:
                sr.setColor(Color.GREEN);
                break;
            case DAMAGE_BOOST:
                sr.setColor(Color.PINK);
                break;
            case SHIELD:
                sr.setColor(Color.BLUE);
                break;
            case AMMO_REFILL:
                sr.setColor(Color.YELLOW);
                break;
        }
        
        // Render using extrinsic state (position and size)
        float halfSize = size / 2;
        float centerX = x + halfSize;
        float centerY = y + halfSize;
        
        // Draw multiple circles for a glowing effect
        sr.circle(centerX, centerY, size * 0.4f);
        sr.circle(centerX, centerY, size * 0.6f);
        sr.circle(centerX, centerY, size * 0.8f);
        
        // Draw square border
        sr.rect(x, y, size, size);
    }
    
    public PowerUpType getIntrinsicState() {
        return intrinsicState;
    }
}
