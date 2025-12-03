package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.mediator.CollisionMediator;
import com.javakaian.shooter.mediator.CollisionEvent;

public class PowerUp {
    
    public enum PowerUpType {
        SPEED_BOOST,
        DAMAGE_BOOST,
        SHIELD,
        AMMO_REFILL
    }
    
    private int id;
    private Vector2 position;
    private PowerUpType type;
    private boolean visible;
    private Rectangle boundRect;
    private float duration; // how long the effect lasts
    private float size;
    private CollisionMediator mediator;
    
    public PowerUp(int id, float x, float y, PowerUpType type, float duration) {
        this.id = id;
        this.position = new Vector2(x, y);
        this.type = type;
        this.duration = duration;
        this.visible = true;
        this.size = 20;
        this.boundRect = new Rectangle(x, y, size, size);
    }
    
    public void update(float deltaTime) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
        
        // Notify mediator that power-up is ready
        if (mediator != null && visible) {
            mediator.notify(this, CollisionEvent.PICKUP_READY);
        }
    }
    
    public void setMediator(CollisionMediator mediator) {
        this.mediator = mediator;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public Vector2 getPosition() { return position; }
    public PowerUpType getType() { return type; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public Rectangle getBoundRect() { return boundRect; }
    public float getDuration() { return duration; }
    public float getSize() { return size; }
    
    @Override
    public String toString() {
        return "PowerUp{id=" + id + ", type=" + type + ", pos=" + position + "}";
    }
}