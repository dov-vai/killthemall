package com.javakaian.shooter.interpreter;

/**
 * TerminalExpression: Represents a god mode command.
 * Syntax: god
 * Example: god
 */
public class GodModeExpression implements Expression {
    
    @Override
    public boolean interpret(Context context) {
        if (context.getPlayer() == null) {
            context.appendOutput("Error: No player found");
            return false;
        }
        
        // Toggle god mode
        boolean currentGodMode = false;
        if (context.hasVariable("godMode")) {
            currentGodMode = (Boolean) context.getVariable("godMode");
        }
        
        boolean newGodMode = !currentGodMode;
        context.setVariable("godMode", newGodMode);
        
        if (newGodMode) {
            context.getPlayer().setHealth(100);
            context.appendOutput("God mode ENABLED - Invincibility activated!");
        } else {
            context.appendOutput("God mode DISABLED");
        }
        
        context.setCommandExecuted(true);
        return true;
    }
}
