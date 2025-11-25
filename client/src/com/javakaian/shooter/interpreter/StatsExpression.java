package com.javakaian.shooter.interpreter;

import com.badlogic.gdx.math.Vector2;

/**
 * TerminalExpression: Displays player statistics.
 * Syntax: stats
 */
public class StatsExpression implements Expression {
    
    @Override
    public boolean interpret(Context context) {
        if (context.getPlayer() == null) {
            context.appendOutput("Error: No player found");
            return false;
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== Player Statistics ===\n");
        stats.append("Health: ").append(context.getPlayer().getHealth()).append("/100\n");
        stats.append("Position: (")
             .append(String.format("%.0f", context.getPlayer().getPosition().x))
             .append(", ")
             .append(String.format("%.0f", context.getPlayer().getPosition().y))
             .append(")\n");
        
        if (context.hasVariable("currentSpeed")) {
            stats.append("Speed: ").append(context.getVariable("currentSpeed")).append("\n");
        }
        
        if (context.hasVariable("godMode") && (Boolean) context.getVariable("godMode")) {
            stats.append("God Mode: ACTIVE");
        }
        
        context.appendOutput(stats.toString());
        context.setCommandExecuted(true);
        return true;
    }
}
