package com.javakaian.shooter.ThemeFactory;
import com.badlogic.gdx.graphics.Color;

public class DarkTheme implements Theme {

    @Override
    public Color getBackgroundColor() {
        return Color.BLACK;
    }

    @Override
    public Color getEnemyColor() {
        return Color.RED;
    }

    @Override
    public Color getBulletColor() {
        return Color.YELLOW;
    }


    @Override
    public Color getTextColor() {
        return Color.WHITE;
    }

    @Override
    public Color getAimLineColor() {
        return Color.WHITE;
    }
}
