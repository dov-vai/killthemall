package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Spike {
    private Vector2 position;
    private float size;

    public Spike(float x, float y, float size) {
        this.position = new Vector2(x, y);
        this.size = size;
    }

    public void render(ShapeRenderer sr) {
        sr.setColor(Color.GOLD);
        sr.circle(position.x + size / 2, position.y + size / 2, size / 2);
        sr.setColor(Color.WHITE);
    }

    public Vector2 getPosition() {
        return position;
    }
}
