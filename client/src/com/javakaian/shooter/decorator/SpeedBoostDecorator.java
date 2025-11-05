package com.javakaian.shooter.decorator;

import com.badlogic.gdx.graphics.Color;
import com.javakaian.shooter.shapes.Player;

/**
 * Speed boost decorator - makes player appear golden/yellow
 */
public class SpeedBoostDecorator extends PlayerDecorator {
    
    public SpeedBoostDecorator(Player player) {
        super(player);
        this.customColor = Color.YELLOW;
        this.decorationType = "Speed Boost";
    }
}
