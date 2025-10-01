package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy implements GameObject{

    private Vector2 position;
    private boolean visible = true;
    private Rectangle boundRect;

    public Enemy(float x, float y, float size) {
        this.position = new Vector2(x, y);
        this.boundRect = new Rectangle(x, y, size, size);
    }

    @Override
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

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBoundRect() {
        return boundRect;
    }
}
