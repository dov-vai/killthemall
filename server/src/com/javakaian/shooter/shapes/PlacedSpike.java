package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.mediator.CollisionMediator;
import com.javakaian.shooter.mediator.CollisionEvent;

/**
 * Represents a spike that has been placed by a player
 * These spikes can damage other players and be undone if not consumed
 */
public class PlacedSpike implements GameObject {
    private Vector2 position;
    private float size;
    private float rotation; // rotation angle in degrees
    private int playerId; // ID of the player who placed this spike
    private boolean visible;
    private boolean consumed; // true if spike has damaged a player
    private Rectangle boundRect;
    private CollisionMediator mediator;

    public PlacedSpike(float x, float y, float size, float rotation, int playerId) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.rotation = rotation;
        this.playerId = playerId;
        this.visible = true;
        this.consumed = false;
        this.boundRect = new Rectangle(x, y, size, size);
    }

    public void update(UpdateContext context) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
        
        // Notify mediator that placed spike is active
        if (mediator != null && visible && !consumed) {
            mediator.notify(this, CollisionEvent.SPIKE_PLACED);
        }
    }
    
    public void setMediator(CollisionMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public boolean isAlive() {
        return !isConsumed();
    }


    public Vector2 getPosition() {
        return position;
    }

    public float getSize() {
        return size;
    }

    public float getRotation() {
        return rotation;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }

    public Rectangle getBoundRect() {
        return boundRect;
    }
}
