package com.javakaian.shooter.shapes;

public interface GameObject {
    void update(float deltaTime);
    boolean isVisible();
    void setVisible(boolean visible);
}
