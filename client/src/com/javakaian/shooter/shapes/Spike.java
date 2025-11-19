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
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.setColor(Color.GOLD);
        sr.circle(position.x + size / 2, position.y + size / 2, size / 2);

        float centerX = position.x + size / 2;
        float centerY = position.y + size / 2;
        float spikeHeight = size * 0.5f;
        float spikeWidth = size * 0.25f;
        float spacing = size * 0.3f;

        sr.setColor(new Color(0.4f, 0.2f, 0.1f, 1f)); // brown

        // Left spike
        float leftX = centerX - spacing;
        sr.triangle(
                leftX, centerY - spikeHeight / 2,
                leftX - spikeWidth / 2, centerY + spikeHeight / 2,
                leftX + spikeWidth / 2, centerY + spikeHeight / 2
        );

        // Middle spike
        sr.triangle(
                centerX, centerY - spikeHeight / 2,
                centerX - spikeWidth / 2, centerY + spikeHeight / 2,
                centerX + spikeWidth / 2, centerY + spikeHeight / 2
        );

        // Right spike
        float rightX = centerX + spacing;
        sr.triangle(
                rightX, centerY - spikeHeight / 2,
                rightX - spikeWidth / 2, centerY + spikeHeight / 2,
                rightX + spikeWidth / 2, centerY + spikeHeight / 2
        );

        sr.setColor(Color.WHITE);
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
    }

    public Vector2 getPosition() {
        return position;
    }
}
