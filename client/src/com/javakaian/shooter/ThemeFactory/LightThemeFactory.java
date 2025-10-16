package com.javakaian.shooter.ThemeFactory;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.AimLine;
import com.javakaian.shooter.shapes.Enemy;

public class LightThemeFactory extends ThemeFactory {

    public LightThemeFactory() {
        Theme theme = createTheme();
        this.enemyPrototype = new Enemy(0, 0, 10, theme.getEnemyColor());
    }

    @Override
    public Theme createTheme() {
        return new LightTheme();
    }

    @Override
    public Enemy createEnemy(float x, float y) {
        return cloneEnemy(x, y);
    }

    @Override
    public AimLine createAimLine(Vector2 begin, Vector2 end) {
        AimLine line = new AimLine(begin, end);
        line.setColor(createTheme().getAimLineColor());
        return line;
    }
}
