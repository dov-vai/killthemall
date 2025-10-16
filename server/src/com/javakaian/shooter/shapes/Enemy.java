package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.strategy.EnemyBehaviorStrategy;

import java.util.List;

public class Enemy{

    private Vector2 position;
    private boolean visible = true;
    private Rectangle boundRect;
    private EnemyBehaviorStrategy behaviorStrategy;

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
    }
    
    public void update(float deltaTime) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
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
    
    public void setBehaviorStrategy(EnemyBehaviorStrategy strategy) {
        this.behaviorStrategy = strategy;
    }
    
    public EnemyBehaviorStrategy getBehaviorStrategy() {
        return behaviorStrategy;
    }
    
    public String getBehaviorName() {
        return behaviorStrategy != null ? behaviorStrategy.getStrategyName() : "None";
    }
}
