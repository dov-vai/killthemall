package com.javakaian.shooter.decorator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.shooter.shapes.Player;

/**
 * Decorator that changes player appearance (size, color)
 * This modifies the Player visually without changing the base class
 */
public class PlayerDecorator {
    
    protected Player player;
    protected Color customColor;
    protected float sizeMultiplier;
    protected String decorationType;
    
    public PlayerDecorator(Player player) {
        this.player = player;
        this.customColor = null; // null means use default
        this.sizeMultiplier = 1.0f;
        this.decorationType = "Normal";
    }
    
    /**
     * Render the decorated player with visual modifications
     */
    public void render(ShapeRenderer sr) {
        // If we have a custom color, save the old color and use ours
        Color oldColor = sr.getColor().cpy();
        
        if (customColor != null) {
            sr.setColor(customColor);
        }
        
        // Render player (possibly with modified size later)
        player.render(sr);
        
        // Restore original color
        if (customColor != null) {
            sr.setColor(oldColor);
        }
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public String getDecorationType() {
        return decorationType;
    }
    
    public void setColor(Color color) {
        this.customColor = color;
    }
    
    public void setSizeMultiplier(float multiplier) {
        this.sizeMultiplier = multiplier;
    }
    
    public float getSizeMultiplier() {
        return sizeMultiplier;
    }
}
