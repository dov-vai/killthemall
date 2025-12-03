package com.javakaian.shooter.interpreter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.states.PlayState;

import java.util.ArrayList;
import java.util.List;

/**
 * GameConsole provides an in-game command console using the Interpreter pattern.
 * Players can press a key (e.g., ~) to open the console and type commands.
 */
public class GameConsole {
    
    private static final int MAX_HISTORY = 10;
    private static final float CONSOLE_HEIGHT = 300;
    private static final float PADDING = 10;
    
    private boolean visible;
    private StringBuilder currentInput;
    private List<String> outputHistory;
    private CommandInterpreter interpreter;
    private Context context;
    private BitmapFont font;
    private int historyOffset;
    
    public GameConsole(PlayState playState, Player player, BitmapFont font) {
        this.visible = false;
        this.currentInput = new StringBuilder();
        this.outputHistory = new ArrayList<>();
        this.interpreter = new CommandInterpreter();
        this.context = new Context(playState, player);
        this.font = font;
        this.historyOffset = 0;
        
        addToHistory("=== Game Console Ready ===");
        addToHistory("Type 'help' for available commands");
    }
    
    public void toggle() {
        visible = !visible;
        if (visible) {
            Gdx.input.setInputProcessor(null); // Will need custom input processor
        }
    }
    
    public void show() {
        visible = true;
    }
    
    public void hide() {
        visible = false;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * Updates the console, handling input.
     */
    public void update() {
        if (!visible) {
            // Check for toggle key (backtick/tilde)
            if (Gdx.input.isKeyJustPressed(Input.Keys.GRAVE)) {
                toggle();
            }
            return;
        }
        
        // Handle console input when visible
        handleInput();
    }
    
    private void handleInput() {
        // Handle special keys
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            executeCommand();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (currentInput.length() > 0) {
                currentInput.deleteCharAt(currentInput.length() - 1);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            hide();
        }
    }
    
    /**
     * Executes the current input command.
     */
    public void executeCommand() {
        String command = currentInput.toString().trim();
        
        if (command.isEmpty()) {
            return;
        }
        
        // Add command to history
        addToHistory("> " + command);
        
        // Clear current input
        currentInput.setLength(0);
        
        // Parse and interpret the command
        Expression expression = interpreter.parse(command);
        
        if (expression != null) {
            context.clearOutput();
            boolean success = expression.interpret(context);
            
            String output = context.getOutput();
            if (!output.isEmpty()) {
                addToHistory(output);
            } else if (!success) {
                addToHistory("Command failed");
            }
        } else {
            addToHistory("Invalid command");
        }
    }
    
    /**
     * Executes a command programmatically (useful for testing).
     */
    public void executeCommandDirect(String command) {
        currentInput.setLength(0);
        currentInput.append(command);
        executeCommand();
    }
    
    private void addToHistory(String text) {
        // Split multi-line output
        String[] lines = text.split("\n");
        for (String line : lines) {
            outputHistory.add(line);
            if (outputHistory.size() > MAX_HISTORY * 3) {
                outputHistory.remove(0);
            }
        }
    }
    
    /**
     * Renders the console UI.
     */
    public void render(ShapeRenderer shapeRenderer) {
        if (!visible) {
            return;
        }
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        // Draw semi-transparent background
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f);
        shapeRenderer.rect(0, screenHeight - CONSOLE_HEIGHT, screenWidth, CONSOLE_HEIGHT);
        
        // Draw border
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rectLine(0, screenHeight - CONSOLE_HEIGHT, screenWidth, 
                              screenHeight - CONSOLE_HEIGHT, 2);
        shapeRenderer.end();
        
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
    }
    
    /**
     * Renders console text.
     */
    public void renderText(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        if (!visible) {
            return;
        }
        
        float screenHeight = Gdx.graphics.getHeight();
        float y = screenHeight - PADDING - 15;
        
        batch.begin();
        
        // Draw output history
        font.setColor(Color.GREEN);
        int startIndex = Math.max(0, outputHistory.size() - 15);
        for (int i = startIndex; i < outputHistory.size(); i++) {
            String line = outputHistory.get(i);
            font.draw(batch, line, PADDING, y);
            y -= 18;
        }
        
        // Draw current input
        font.setColor(Color.WHITE);
        String inputLine = "> " + currentInput.toString() + "_";
        font.draw(batch, inputLine, PADDING, screenHeight - CONSOLE_HEIGHT + 25);
        
        // Draw help text
        font.setColor(Color.GRAY);
        font.getData().setScale(0.7f);
        font.draw(batch, "Press ~ to toggle console | ESC to close | Type 'help' for commands", 
                 PADDING, screenHeight - CONSOLE_HEIGHT + 10);
        font.getData().setScale(1f);
        
        batch.end();
    }
    
    public Context getContext() {
        return context;
    }
    
    public void appendInput(char character) {
        currentInput.append(character);
    }
    
    public void backspace() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
        }
    }
}
