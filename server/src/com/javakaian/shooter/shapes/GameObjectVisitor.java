package com.javakaian.shooter.shapes;

public interface GameObjectVisitor {
    void visit(Player player);
    void visit(Enemy enemy);
    void visit(Bullet bullet);
    void visit(Spike spike);
    void visit(PlacedSpike spike);
}
