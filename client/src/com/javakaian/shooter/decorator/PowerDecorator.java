package com.javakaian.shooter.decorator;

import com.badlogic.gdx.graphics.Color;
import com.javakaian.shooter.shapes.Player;

/**
 * Power decorator - makes player appear red/powerful
 */
public class PowerDecorator extends PlayerDecorator {
    
    public PowerDecorator(Player player) {
        super(player);
        this.customColor = Color.RED;
        this.decorationType = "Power";
    }
}
