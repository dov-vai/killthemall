package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.flyweight.Flyweight;
import com.javakaian.shooter.shapes.flyweight.FlyweightFactory;

/**
 * PowerUp now acts as a Client in the Flyweight pattern.
 * It stores extrinsic state (position, size) and uses a shared
 * Flyweight object for rendering based on the PowerUpType.
 */
public class PowerUp {
    
    public enum PowerUpType {
        SPEED_BOOST,      // 0 - Green
        DAMAGE_BOOST,     // 1 - Pink
        SHIELD,           // 2 - Cyan
        AMMO_REFILL       // 3 - Yellow
    }
    
    private static FlyweightFactory factory = new FlyweightFactory();
    
    // Extrinsic state (unique to each PowerUp instance)
    private Vector2 position;
    private float size;
    
    // Reference to the type, used to get the appropriate flyweight
    private PowerUpType type;
    
    public PowerUp(float x, float y, float size, PowerUpType type) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.type = type;
    }
    
    /**
     * Render method now delegates to the Flyweight object,
     * passing the extrinsic state (position and size).
     */
    public void render(ShapeRenderer sr) {
        // Get the shared flyweight for this PowerUpType
        Flyweight flyweight = factory.getFlyweight(type);
        
        // Call operation with extrinsic state
        flyweight.operation(sr, position.x, position.y, size);
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
    
    /**
     * Returns the FlyweightFactory instance for debugging or testing purposes.
     */
    public static FlyweightFactory getFactory() {
        return factory;
    }
}