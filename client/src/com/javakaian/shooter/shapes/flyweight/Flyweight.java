package com.javakaian.shooter.shapes.flyweight;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * The Flyweight interface declares operations that can accept extrinsic state.
 * In this case, the extrinsic state is the position and size of the PowerUp.
 */
public interface Flyweight {
    
    /**
     * Performs an operation using the provided extrinsic state.
     * 
     * @param sr The ShapeRenderer used for rendering
     * @param x The x-coordinate of the PowerUp (extrinsic state)
     * @param y The y-coordinate of the PowerUp (extrinsic state)
     * @param size The size of the PowerUp (extrinsic state)
     */
    void operation(ShapeRenderer sr, float x, float y, float size);
}
