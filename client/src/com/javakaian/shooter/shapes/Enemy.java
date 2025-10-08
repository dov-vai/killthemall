package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Enemy {

    private Vector2 position;
    private float size;
    private boolean visible = true;
    private Color color;

    public Enemy(float x, float y, float size) {
        this(x, y, size, Color.CYAN);
    }

    public Enemy(float x, float y, float size, Color color) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.color = color;
    }

    public void render(ShapeRenderer sr) {
        if (!visible) return;
        sr.setColor(color);
        sr.circle(position.x, position.y, size);
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
