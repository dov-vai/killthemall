package com.javakaian.shooter.interpreter;

/**
 * TerminalExpression: Displays help information.
 * Syntax: help
 */
public class HelpExpression implements Expression {
    
    @Override
    public boolean interpret(Context context) {
        StringBuilder help = new StringBuilder();
        help.append("=== Available Commands ===\n");
        help.append("heal <amount>    - Restore health points\n");
        help.append("speed <mult>     - Set speed multiplier\n");
        help.append("god              - Toggle god mode\n");
        help.append("stats            - Show player statistics\n");
        help.append("teleport <x> <y> - Teleport to position\n");
        help.append("flyweight (fw)   - Show Flyweight pattern stats\n");
        help.append("help             - Show this help message\n");
        help.append("clear            - Clear console output");
        
        context.appendOutput(help.toString());
        context.setCommandExecuted(true);
        return true;
    }
}
