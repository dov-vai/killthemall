package com.javakaian.shooter.interpreter;

import com.javakaian.shooter.shapes.Player;
import com.javakaian.states.PlayState;

import java.util.HashMap;
import java.util.Map;

/**
 * Context holds the global information needed by the interpreter.
 * It stores variables, game state references, and provides methods
 * for expressions to interact with the game.
 */
public class Context {
    
    private Map<String, Object> variables;
    private PlayState playState;
    private Player player;
    private StringBuilder output;
    private boolean commandExecuted;
    
    public Context(PlayState playState, Player player) {
        this.playState = playState;
        this.player = player;
        this.variables = new HashMap<>();
        this.output = new StringBuilder();
        this.commandExecuted = false;
    }
    
    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }
    
    public Object getVariable(String name) {
        return variables.get(name);
    }
    
    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }
    
    public PlayState getPlayState() {
        return playState;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void appendOutput(String message) {
        if (output.length() > 0) {
            output.append("\n");
        }
        output.append(message);
    }
    
    public String getOutput() {
        return output.toString();
    }
    
    public void clearOutput() {
        output.setLength(0);
    }
    
    public boolean isCommandExecuted() {
        return commandExecuted;
    }
    
    public void setCommandExecuted(boolean executed) {
        this.commandExecuted = executed;
    }
}
