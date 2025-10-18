package com.javakaian.shooter.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class PlacedSpike {
    private Vector2 position;
    private float size;
    private float rotation;

    public PlacedSpike(float x, float y, float size, float rotation) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.rotation = rotation;
    }

    public void render(ShapeRenderer sr) {
        sr.end();
        sr.begin(ShapeType.Filled);
        
        // Draw spike as a triangle pointing in the rotation direction
        float centerX = position.x + size / 2;
        float centerY = position.y + size / 2;
        
        // Convert rotation to radians
        float angleRad = (float) Math.toRadians(rotation);
        
        // Calculate triangle points
        float tipX = centerX + (float) Math.cos(angleRad) * size;
        float tipY = centerY - (float) Math.sin(angleRad) * size;
        
        float baseAngle1 = angleRad + (float) Math.toRadians(120);
        float baseAngle2 = angleRad - (float) Math.toRadians(120);
        
        float base1X = centerX + (float) Math.cos(baseAngle1) * size * 0.6f;
        float base1Y = centerY - (float) Math.sin(baseAngle1) * size * 0.6f;
        
        float base2X = centerX + (float) Math.cos(baseAngle2) * size * 0.6f;
        float base2Y = centerY - (float) Math.sin(baseAngle2) * size * 0.6f;
        
        sr.setColor(Color.RED);
        sr.triangle(tipX, tipY, base1X, base1Y, base2X, base2Y);
        sr.setColor(Color.WHITE);
        
        sr.end();
        sr.begin(ShapeType.Line);
    }

    public Vector2 getPosition() {
        return position;
    }
}
