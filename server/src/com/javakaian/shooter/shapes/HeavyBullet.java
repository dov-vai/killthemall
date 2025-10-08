package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class HeavyBullet implements Bullet {
    
    private Vector2 position;
    private float size;
    private float angle;
    private boolean visible = true;
    private float ttlCounter = 0;
    private int id;
    private Rectangle boundRect;
    
    private static final int SPEED = 500;  // slower speed

    public HeavyBullet(float x, float y, float size, float angle, int id) {
        this.position = new Vector2(x, y);
        this.size = size * 1.5f;  // size change into bigger
        this.angle = angle;
        this.id = id;
        this.boundRect = new Rectangle(x, y, this.size, this.size);
    }

    @Override
    public void update(float deltaTime) {
        float speed = deltaTime * SPEED;
        this.ttlCounter += deltaTime;

        float dx = (float) Math.cos(angle);
        float dy = (float) Math.sin(angle);

        position.y -= speed * dy;
        position.x += speed * dx;
        
        if (ttlCounter > 3) {  // lives longer
            visible = false;
            ttlCounter = 0;
        }

        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getSize() {
        return size;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Rectangle getBoundRect() {
        return boundRect;
    }
}