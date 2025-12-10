package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.mediator.CollisionMediator;
import com.javakaian.shooter.mediator.CollisionEvent;

/**
 * Represents a spike pickup in the world that players can collect
 */
public class Spike implements GameObject {
    private Vector2 position;
    private float size;
    private boolean visible;
    private Rectangle boundRect;
    private CollisionMediator mediator;

    public Spike(float x, float y, float size) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.visible = true;
        this.boundRect = new Rectangle(x, y, size, size);
    }

    public void update(UpdateContext context) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
        
        // Notify mediator that spike pickup is ready
        if (mediator != null && visible) {
            mediator.notify(this, CollisionEvent.PICKUP_READY);
        }
    }
    
    public void setMediator(CollisionMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public boolean isAlive() {
        return isVisible();
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSize() {
        return size;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBoundRect() {
        return boundRect;
    }
}
