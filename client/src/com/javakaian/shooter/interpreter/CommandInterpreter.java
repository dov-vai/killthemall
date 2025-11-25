package com.javakaian.shooter.interpreter;

/**
 * CommandInterpreter parses text commands and builds an expression tree.
 * This is the Client in the Interpreter pattern (GoF).
 * 
 * Grammar:
 * <command> ::= <heal> | <speed> | <god> | <help> | <stats> | <teleport>
 * <heal> ::= "heal" <number>
 * <speed> ::= "speed" <number>
 * <god> ::= "god"
 * <help> ::= "help"
 * <stats> ::= "stats"
 * <teleport> ::= "teleport" <number> <number>
 * <number> ::= integer value
 */
public class CommandInterpreter {
    
    /**
     * Parses a command string and returns an Expression tree.
     * 
     * @param commandText The command text to parse
     * @return An Expression representing the parsed command, or null if invalid
     */
    public Expression parse(String commandText) {
        if (commandText == null || commandText.trim().isEmpty()) {
            return null;
        }
        
        // Split by semicolon for command sequences
        String[] commands = commandText.split(";");
        
        if (commands.length == 1) {
            // Single command
            return parseSingleCommand(commandText.trim());
        } else {
            // Multiple commands - use SequenceExpression
            SequenceExpression sequence = new SequenceExpression();
            for (String cmd : commands) {
                Expression expr = parseSingleCommand(cmd.trim());
                if (expr != null) {
                    sequence.addExpression(expr);
                }
            }
            return sequence;
        }
    }
    
    /**
     * Parses a single command into an Expression.
     */
    private Expression parseSingleCommand(String commandText) {
        String[] tokens = commandText.toLowerCase().split("\\s+");
        
        if (tokens.length == 0) {
            return null;
        }
        
        String command = tokens[0];
        
        switch (command) {
            case "help":
                return new HelpExpression();
                
            case "stats":
                return new StatsExpression();
                
            case "god":
                return new GodModeExpression();
                
            case "heal":
                if (tokens.length < 2) {
                    return createErrorExpression("heal requires an amount. Usage: heal <amount>");
                }
                try {
                    int amount = Integer.parseInt(tokens[1]);
                    return new HealExpression(new NumberExpression(amount));
                } catch (NumberFormatException e) {
                    return createErrorExpression("Invalid heal amount: " + tokens[1]);
                }
                
            case "speed":
                if (tokens.length < 2) {
                    return createErrorExpression("speed requires a multiplier. Usage: speed <multiplier>");
                }
                try {
                    int multiplier = Integer.parseInt(tokens[1]);
                    return new SpeedExpression(new NumberExpression(multiplier));
                } catch (NumberFormatException e) {
                    return createErrorExpression("Invalid speed multiplier: " + tokens[1]);
                }
                
            case "teleport":
            case "tp":
                if (tokens.length < 3) {
                    return createErrorExpression("teleport requires X and Y coordinates. Usage: teleport <x> <y>");
                }
                try {
                    int x = Integer.parseInt(tokens[1]);
                    int y = Integer.parseInt(tokens[2]);
                    return new TeleportExpression(new NumberExpression(x), new NumberExpression(y));
                } catch (NumberFormatException e) {
                    return createErrorExpression("Invalid teleport coordinates");
                }
                
            case "clear":
                return new ClearExpression();
                
            default:
                return createErrorExpression("Unknown command: " + command + ". Type 'help' for available commands.");
        }
    }
    
    /**
     * Creates an expression that outputs an error message.
     */
    private Expression createErrorExpression(final String errorMessage) {
        return new Expression() {
            @Override
            public boolean interpret(Context context) {
                context.appendOutput("Error: " + errorMessage);
                return false;
            }
        };
    }
}
