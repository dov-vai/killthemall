package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Enemy {

    private Vector2 position;
    private float size;
    private boolean visible = true;

    public Enemy(float x, float y, float size) {
        this.position = new Vector2(x, y);
        this.size = size;
    }

    public void render(ShapeRenderer sr) {
        sr.setColor(Color.CYAN);
        sr.circle(position.x, position.y, size);
        sr.setColor(Color.WHITE);
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

}
