package com.javakaian.shooter.interpreter;

/**
 * TerminalExpression: Represents a heal command.
 * Syntax: heal <amount>
 * Example: heal 50
 */
public class HealExpression implements Expression {
    
    private Expression amountExpression;
    
    public HealExpression(Expression amountExpression) {
        this.amountExpression = amountExpression;
    }
    
    @Override
    public boolean interpret(Context context) {
        if (context.getPlayer() == null) {
            context.appendOutput("Error: No player found");
            return false;
        }
        
        // Interpret the amount expression first
        if (!amountExpression.interpret(context)) {
            context.appendOutput("Error: Invalid heal amount");
            return false;
        }
        
        Object lastNumber = context.getVariable("lastNumber");
        if (!(lastNumber instanceof Integer)) {
            context.appendOutput("Error: Heal amount must be a number");
            return false;
        }
        
        int amount = (Integer) lastNumber;
        int currentHealth = context.getPlayer().getHealth();
        int newHealth = Math.min(100, currentHealth + amount);
        
        context.getPlayer().setHealth(newHealth);
        context.appendOutput("Healed " + amount + " HP. Current health: " + newHealth);
        context.setCommandExecuted(true);
        
        return true;
    }
}
