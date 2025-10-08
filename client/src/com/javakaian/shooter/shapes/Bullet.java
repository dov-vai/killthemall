package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    private Vector2 position;
    private float size;
    private boolean visible = true;
    private Color color;


    public Bullet(float x, float y, float size) {
        this.position = new Vector2(x, y);
        this.size = size;
    }

    public void render(ShapeRenderer sr) {
        sr.setColor(this.color);
        sr.rect(position.x, position.y, size, size);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
