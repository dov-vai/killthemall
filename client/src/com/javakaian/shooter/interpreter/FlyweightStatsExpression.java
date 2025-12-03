package com.javakaian.shooter.interpreter;

import com.javakaian.shooter.shapes.PowerUp;
import com.javakaian.shooter.shapes.flyweight.FlyweightFactory;

/**
 * TerminalExpression: Displays Flyweight pattern statistics.
 * Shows the number of shared flyweight instances vs total PowerUp objects.
 * Syntax: flyweight
 */
public class FlyweightStatsExpression implements Expression {
    
    @Override
    public boolean interpret(Context context) {
        FlyweightFactory factory = PowerUp.getFactory();
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== Flyweight Pattern Stats ===\n");
        stats.append("Shared Flyweight instances: ").append(factory.getFlyweightCount()).append("\n");
        stats.append("PowerUp types using flyweights:\n");
        stats.append("  - SPEED_BOOST (Green)\n");
        stats.append("  - DAMAGE_BOOST (Pink)\n");
        stats.append("  - SHIELD (Blue)\n");
        stats.append("  - AMMO_REFILL (Yellow)\n");
        stats.append("Memory saved: Instead of duplicating\n");
        stats.append("  rendering logic per PowerUp, only\n");
        stats.append("  ").append(factory.getFlyweightCount()).append(" shared object(s) are used!");
        
        context.appendOutput(stats.toString());
        context.setCommandExecuted(true);
        return true;
    }
}
