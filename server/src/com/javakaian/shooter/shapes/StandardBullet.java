package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class StandardBullet implements Bullet, GameObject {
    private static final int SPEED = 800;  // normal speed
    private Vector2 position;
    private float size;
    private float angle;
    private boolean visible = true;
    private float ttlCounter = 0;
    private int id;
    private Rectangle boundRect;

    public StandardBullet(float x, float y, float size, float angle, int id) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.angle = angle;
        this.id = id;
        this.boundRect = new Rectangle(x, y, size, size);
    }

    @Override
    public void update(UpdateContext context) {
        float speed = context.deltaTime * SPEED;
        this.ttlCounter += context.deltaTime;

        float dx = (float) Math.cos(angle);
        float dy = (float) Math.sin(angle);

        position.y -= speed * dy;
        position.x += speed * dx;

        if (ttlCounter > 2) {
            visible = false;
            ttlCounter = 0;
        }

        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
    }

    @Override
    public void accept(GameObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isAlive() {
        return isVisible();
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