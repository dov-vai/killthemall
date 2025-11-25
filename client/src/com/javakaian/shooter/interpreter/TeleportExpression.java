package com.javakaian.shooter.interpreter;

import com.badlogic.gdx.math.Vector2;

/**
 * NonTerminalExpression: Represents a teleport command with two arguments.
 * Syntax: teleport <x> <y>
 * Example: teleport 500 300
 * 
 * This is a composite expression that contains two NumberExpressions.
 */
public class TeleportExpression implements Expression {
    
    private Expression xExpression;
    private Expression yExpression;
    
    public TeleportExpression(Expression xExpression, Expression yExpression) {
        this.xExpression = xExpression;
        this.yExpression = yExpression;
    }
    
    @Override
    public boolean interpret(Context context) {
        if (context.getPlayer() == null) {
            context.appendOutput("Error: No player found");
            return false;
        }
        
        // Interpret X coordinate
        if (!xExpression.interpret(context)) {
            context.appendOutput("Error: Invalid X coordinate");
            return false;
        }
        int x = (Integer) context.getVariable("lastNumber");
        
        // Interpret Y coordinate
        if (!yExpression.interpret(context)) {
            context.appendOutput("Error: Invalid Y coordinate");
            return false;
        }
        int y = (Integer) context.getVariable("lastNumber");
        
        // Set player position
        Vector2 newPosition = new Vector2(x, y);
        context.getPlayer().setPosition(newPosition);
        
        context.appendOutput("Teleported to (" + x + ", " + y + ")");
        context.setCommandExecuted(true);
        
        return true;
    }
}
