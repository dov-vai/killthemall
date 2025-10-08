package com.javakaian.shooter.ThemeFactory;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.AimLine;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.network.messages.*;

import java.util.ArrayList;
import java.util.List;

public abstract class ThemeFactory {
    public abstract Theme createTheme();
    public abstract Enemy createEnemy(float x, float y);
    public abstract AimLine createAimLine(Vector2 begin, Vector2 end);

    public static ThemeFactory getFactory(boolean darkMode) {
        if (darkMode) {
            return new DarkThemeFactory();
        } else {
            return new LightThemeFactory();
        }
    }

    public List<Enemy> createEnemiesFromGWM(GameWorldMessage m) {
        float[] temp = m.getEnemies();
        List<Enemy> enemies = new ArrayList<>();

        for (int i = 0; i < temp.length / 2; i++) {
            float x = temp[i * 2];
            float y = temp[i * 2 + 1];
            enemies.add(createEnemy(x, y));
        }
        return enemies;
    }
}
