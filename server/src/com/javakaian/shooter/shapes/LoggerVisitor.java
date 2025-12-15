package com.javakaian.shooter.shapes;

public class LoggerVisitor implements GameObjectVisitor {

    @Override
    public void visit(Player player) {
        System.out.println("Player " + player.getId() + " at position " + player.getPosition());
    }

    @Override
    public void visit(Enemy enemy) {
    }

    @Override
    public void visit(Bullet bullet) {
    }

    @Override
    public void visit(Spike spike) {
    }

    @Override
    public void visit(PlacedSpike spike) {
        System.out.println("PlacedSpike at position " + spike.getPosition());
    }
}
