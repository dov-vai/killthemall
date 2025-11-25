package com.javakaian.shooter.interpreter;

/**
 * The AbstractExpression interface declares an interpret operation
 * that is common to all nodes in the abstract syntax tree.
 * 
 * This is the core of the Interpreter pattern (GoF).
 */
public interface Expression {
    
    /**
     * Interprets the expression in the given context.
     * 
     * @param context The context containing game state and variables
     * @return true if the expression was successfully interpreted, false otherwise
     */
    boolean interpret(Context context);
}
