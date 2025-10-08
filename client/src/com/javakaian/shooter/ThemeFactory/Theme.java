package com.javakaian.shooter.ThemeFactory;

import com.badlogic.gdx.graphics.Color;

public abstract class Theme {
    protected Color backgroundColor;
    protected Color enemyColor;
    protected Color textColor;
    protected Color aimLineColor;

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getEnemyColor() {
        return enemyColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getAimLineColor() {
        return aimLineColor;
    }
}
