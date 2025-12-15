package com.javakaian.shooter.shapes;

public interface GameObject {
    void update(UpdateContext context);
    boolean isAlive();
    public abstract void accept(GameObjectVisitor visitor);
}
