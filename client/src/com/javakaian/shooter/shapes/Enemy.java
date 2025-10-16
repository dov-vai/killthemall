package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Enemy implements Cloneable {

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Enemy clone() {
        try {
            Enemy copy = (Enemy) super.clone();
            copy.position = new Vector2(this.position);
            copy.color = new Color(this.color);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
