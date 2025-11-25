package com.javakaian.shooter.interpreter;

/**
 * TerminalExpression: Represents a literal number value.
 * This is a leaf node in the expression tree.
 */
public class NumberExpression implements Expression {
    
    private int number;
    
    public NumberExpression(int number) {
        this.number = number;
    }
    
    @Override
    public boolean interpret(Context context) {
        // Store the number in context for use by other expressions
        context.setVariable("lastNumber", number);
        return true;
    }
    
    public int getNumber() {
        return number;
    }
}
