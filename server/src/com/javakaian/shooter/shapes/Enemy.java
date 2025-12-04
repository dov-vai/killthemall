package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.strategy.EnemyBehaviorStrategy;
import com.javakaian.shooter.mediator.CollisionMediator;
import com.javakaian.shooter.mediator.CollisionEvent;

import java.util.List;

public class Enemy implements Cloneable {

    private Vector2 position;
    private boolean visible = true;
    private Rectangle boundRect;
    private EnemyBehaviorStrategy behaviorStrategy;
    private CollisionMediator mediator;

    public Enemy(float x, float y, float size) {
        this.position = new Vector2(x, y);
        this.boundRect = new Rectangle(x, y, size, size);
    }

    public Enemy(float x, float y, float size, EnemyBehaviorStrategy strategy) {
        this.position = new Vector2(x, y);
        this.boundRect = new Rectangle(x, y, size, size);
        this.behaviorStrategy = strategy;
    }

    public void update(float deltaTime, List<Player> players) {
        if (behaviorStrategy != null && players != null) {
            position = behaviorStrategy.behaveDifferently(position, players, deltaTime);
        }

        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
        
        // Notify mediator that enemy has moved
        if (mediator != null) {
            mediator.notify(this, CollisionEvent.MOVED);
        }
    }

    public void update(float deltaTime) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
        
        // Notify mediator that enemy has moved
        if (mediator != null) {
            mediator.notify(this, CollisionEvent.MOVED);
        }
    }
    
    public void setMediator(CollisionMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public Enemy clone() {
        try {
            return (Enemy) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
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

    public EnemyBehaviorStrategy getBehaviorStrategy() {
        return behaviorStrategy;
    }

    public void setBehaviorStrategy(EnemyBehaviorStrategy strategy) {
        this.behaviorStrategy = strategy;
    }

    public String getBehaviorName() {
        return behaviorStrategy != null ? behaviorStrategy.getStrategyName() : "None";
    }
}
