package com.javakaian.shooter.ThemeFactory;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.AimLine;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Enemy;

public class DarkThemeFactory extends ThemeFactory {

    @Override
    public Theme createTheme() {
        return new DarkTheme();
    }

    @Override
    public Enemy createEnemy(float x, float y) {
        Theme theme = createTheme();

        Enemy e = new Enemy(x, y, 10);
        e.setColor(theme.getEnemyColor());
        return e;
    }

    @Override
    public Bullet createBullet(float x, float y, float size) {
        Bullet b = new Bullet(x, y, size);
        b.setColor(createTheme().getBulletColor());
        return b;
    }

    @Override
    public AimLine createAimLine(Vector2 begin, Vector2 end) {
        Theme theme = createTheme();

        AimLine line = new AimLine(begin, end);
        line.setColor(theme.getAimLineColor());
        return line;
    }
}