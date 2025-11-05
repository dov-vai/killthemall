package com.javakaian.shooter.decorator;

import com.badlogic.gdx.graphics.Color;
import com.javakaian.shooter.shapes.Player;

/**
 * Shield decorator - makes player appear cyan/light blue
 */
public class ShieldDecorator extends PlayerDecorator {
    
    public ShieldDecorator(Player player) {
        super(player);
        this.customColor = Color.CYAN;
        this.decorationType = "Shield";
    }
}
