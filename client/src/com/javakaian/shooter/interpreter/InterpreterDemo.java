package com.javakaian.shooter.interpreter;

import com.javakaian.shooter.shapes.Player;

/**
 * InterpreterDemo demonstrates the Interpreter pattern implementation.
 * Shows how commands are parsed and executed through the expression tree.
 */
public class InterpreterDemo {
    
    public static void main(String[] args) {
        runDemo();
    }
    
    public static void runDemo() {
        System.out.println("=== Interpreter Pattern Demonstration ===\n");
        
        // Create a mock player for testing
        Player mockPlayer = new Player(100, 100, 50);
        mockPlayer.setHealth(50);
        mockPlayer.setId(1);
        
        // Create context (PlayState would be null in this demo)
        Context context = new Context(null, mockPlayer);
        
        // Create interpreter
        CommandInterpreter interpreter = new CommandInterpreter();
        
        // Test various commands
        testCommand(interpreter, context, "help");
        testCommand(interpreter, context, "heal 50");
        testCommand(interpreter, context, "stats");
        testCommand(interpreter, context, "speed 3");
        testCommand(interpreter, context, "teleport 500 300");
        testCommand(interpreter, context, "god");
        testCommand(interpreter, context, "stats");
        
        // Test command sequences
        System.out.println("\n=== Testing Command Sequences ===\n");
        testCommand(interpreter, context, "heal 25; speed 2; stats");
        
        // Test error handling
        System.out.println("\n=== Testing Error Handling ===\n");
        testCommand(interpreter, context, "invalid");
        testCommand(interpreter, context, "heal");
        testCommand(interpreter, context, "teleport 100");
        
        System.out.println("\n=== Expression Tree Structure ===\n");
        demonstrateExpressionTree(interpreter);
        
        System.out.println("\n=== End of Demonstration ===");
    }
    
    private static void testCommand(CommandInterpreter interpreter, Context context, 
                                    String command) {
        System.out.println("Command: " + command);
        
        // Parse command into expression tree
        Expression expression = interpreter.parse(command);
        
        if (expression != null) {
            // Clear previous output
            context.clearOutput();
            
            // Interpret the expression
            boolean success = expression.interpret(context);
            
            // Display output
            String output = context.getOutput();
            if (!output.isEmpty()) {
                System.out.println("Output:\n" + output);
            }
            System.out.println("Success: " + success);
        } else {
            System.out.println("Output: Failed to parse command");
        }
        
        System.out.println();
    }
    
    private static void demonstrateExpressionTree(CommandInterpreter interpreter) {
        System.out.println("Parsing: 'teleport 500 300'");
        System.out.println("Creates expression tree:");
        System.out.println();
        System.out.println("         TeleportExpression");
        System.out.println("         /              \\");
        System.out.println("  NumberExpression   NumberExpression");
        System.out.println("      (500)              (300)");
        System.out.println();
        
        System.out.println("Parsing: 'heal 50; speed 2'");
        System.out.println("Creates expression tree:");
        System.out.println();
        System.out.println("       SequenceExpression");
        System.out.println("         /          \\");
        System.out.println("  HealExpression  SpeedExpression");
        System.out.println("       |                |");
        System.out.println(" NumberExpression  NumberExpression");
        System.out.println("     (50)              (2)");
        System.out.println();
        
        System.out.println("This tree structure follows the Interpreter pattern (GoF):");
        System.out.println("- Expression: Abstract interface");
        System.out.println("- Terminal Expressions: NumberExpression, GodModeExpression, etc.");
        System.out.println("- NonTerminal Expressions: TeleportExpression, SequenceExpression");
        System.out.println("- Context: Stores variables and game state");
        System.out.println("- Client: CommandInterpreter (parses text into expression tree)");
    }
}
