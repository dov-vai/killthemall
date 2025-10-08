package com.javakaian.shooter.ThemeFactory;

import com.badlogic.gdx.graphics.Color;

public class LightTheme implements Theme {

    @Override
    public Color getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public Color getEnemyColor() {
        return Color.BLACK;
    }

    @Override
    public Color getBulletColor() {
        return Color.SCARLET;
    }

    @Override
    public Color getTextColor() {
        return Color.BLACK;
    }
}
