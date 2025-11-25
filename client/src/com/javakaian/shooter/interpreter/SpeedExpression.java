package com.javakaian.shooter.interpreter;

import com.badlogic.gdx.math.Vector2;

/**
 * TerminalExpression: Represents a speed boost command.
 * Syntax: speed <multiplier>
 * Example: speed 2
 */
public class SpeedExpression implements Expression {
    
    private Expression multiplierExpression;
    
    public SpeedExpression(Expression multiplierExpression) {
        this.multiplierExpression = multiplierExpression;
    }
    
    @Override
    public boolean interpret(Context context) {
        if (context.getPlayer() == null) {
            context.appendOutput("Error: No player found");
            return false;
        }
        
        // Interpret the multiplier expression first
        if (!multiplierExpression.interpret(context)) {
            context.appendOutput("Error: Invalid speed multiplier");
            return false;
        }
        
        Object lastNumber = context.getVariable("lastNumber");
        if (!(lastNumber instanceof Integer)) {
            context.appendOutput("Error: Speed multiplier must be a number");
            return false;
        }
        
        int multiplier = (Integer) lastNumber;
        
        // Store original speed if not already stored
        if (!context.hasVariable("originalSpeed")) {
            context.setVariable("originalSpeed", 200); // Default player speed
        }
        
        int originalSpeed = (Integer) context.getVariable("originalSpeed");
        int newSpeed = originalSpeed * multiplier;
        
        // Note: This is a demonstration. Actual speed modification would
        // require accessing the player's movement system
        context.setVariable("currentSpeed", newSpeed);
        context.appendOutput("Speed multiplier set to " + multiplier + "x (Speed: " + newSpeed + ")");
        context.setCommandExecuted(true);
        
        return true;
    }
}
