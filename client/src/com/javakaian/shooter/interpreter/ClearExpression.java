package com.javakaian.shooter.interpreter;

/**
 * TerminalExpression: Clears the console output.
 * Syntax: clear
 */
public class ClearExpression implements Expression {
    
    @Override
    public boolean interpret(Context context) {
        context.clearOutput();
        context.setCommandExecuted(true);
        return true;
    }
}
