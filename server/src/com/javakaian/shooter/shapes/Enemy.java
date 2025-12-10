package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.strategy.EnemyBehaviorStrategy;

import java.util.List;

public class Enemy implements Cloneable, GameObject {

    private Vector2 position;
    private boolean visible = true;
    private Rectangle boundRect;
    private EnemyBehaviorStrategy behaviorStrategy;

    public Enemy(float x, float y, float size, EnemyBehaviorStrategy strategy) {
        this.position = new Vector2(x, y);
        this.boundRect = new Rectangle(x, y, size, size);
        this.behaviorStrategy = strategy;
    }

    @Override
    public void update(UpdateContext context) {
        if (!visible) return;

        if (behaviorStrategy != null && context.players != null && !context.players.isEmpty()) {
            position = behaviorStrategy.behaveDifferently(position, context.players, context.deltaTime);
        }

        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
    }

    @Override
    public boolean isAlive() {
        return isVisible();
    }

    public void update(float deltaTime) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
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
