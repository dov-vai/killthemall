package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class PowerUp {
    
    public enum PowerUpType {
        SPEED_BOOST,      // 0 - Green
        DAMAGE_BOOST,     // 1 - Pink
        SHIELD,           // 2 - Cyan
        AMMO_REFILL       // 3 - Yellow
    }
    
    private Vector2 position;
    private float size;
    private PowerUpType type;
    
    public PowerUp(float x, float y, float size, PowerUpType type) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.type = type;
    }
    
    public void render(ShapeRenderer sr) {
        switch (type) {
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
        
        float halfSize = size / 2;
        float centerX = position.x + halfSize;
        float centerY = position.y + halfSize;
        
        // Draw multiple circles for a glowing effect (all in Line mode)
        sr.circle(centerX, centerY, size * 0.4f);
        sr.circle(centerX, centerY, size * 0.6f);
        sr.circle(centerX, centerY, size * 0.8f);
        
        // Draw square border
        sr.rect(position.x, position.y, size, size);
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public float getSize() {
        return size;
    }
    
    public PowerUpType getType() {
        return type;
    }
}