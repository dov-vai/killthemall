package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Bullet {

    void update(float deltaTime);
    Vector2 getPosition();
    float getSize();
    boolean isVisible();
    void setVisible(boolean visible);
    int getId();
    Rectangle getBoundRect();

}
