package com.javakaian.shooter.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * NonTerminalExpression: Represents a sequence of commands.
 * This allows multiple commands to be executed in order.
 * 
 * Example: "heal 50; speed 2; stats"
 */
public class SequenceExpression implements Expression {
    
    private List<Expression> expressions;
    
    public SequenceExpression() {
        this.expressions = new ArrayList<>();
    }
    
    public void addExpression(Expression expression) {
        expressions.add(expression);
    }
    
    @Override
    public boolean interpret(Context context) {
        boolean allSuccessful = true;
        
        for (Expression expression : expressions) {
            if (!expression.interpret(context)) {
                allSuccessful = false;
                // Continue executing other commands even if one fails
            }
        }
        
        return allSuccessful;
    }
    
    public int size() {
        return expressions.size();
    }
}
