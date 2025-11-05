package com.javakaian.shooter.logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.shooter.utils.GameConstants;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.GameUtils;

import java.util.List;

/**
 * Simple in-game log display overlay.
 * Shows captured logs in a chat-like window.
 */
public class SimpleLogDisplay {
    
    private boolean visible;
    private BitmapFont font;
    private BitmapFont titleFont;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera uiCamera; // Fixed UI camera
    private float updateTimer;
    private List<String> cachedLogs;
    
    // Window properties
    private float windowX = 20;
    private float windowY = 20;
    private float windowWidth = 600;
    private float windowHeight = 400;
    
    public SimpleLogDisplay() {
        this.visible = false;
        
        // Create readable fonts
        this.titleFont = GameManagerFacade.getInstance().generateBitmapFont(20, Color.GOLD);
        this.font = GameManagerFacade.getInstance().generateBitmapFont(14, Color.WHITE);
        
        this.shapeRenderer = new ShapeRenderer();
        
        // Create a FIXED UI camera that doesn't move
        this.uiCamera = new OrthographicCamera(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        this.uiCamera.setToOrtho(true); // Match PlayState's orientation
        this.uiCamera.position.set(GameConstants.SCREEN_WIDTH / 2f, GameConstants.SCREEN_HEIGHT / 2f, 0);
        this.uiCamera.update();
        
        this.updateTimer = 0;
        this.cachedLogs = LogCapture.getInstance().getLogs();
    }
    
    public void toggle() {
        visible = !visible;
        if (visible) {
            updateLogs();
        }
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void update(float deltaTime) {
        if (!visible) return;
        
        updateTimer += deltaTime;
        if (updateTimer >= 0.2f) {
            updateLogs();
            updateTimer = 0;
        }
    }
    
    private void updateLogs() {
        cachedLogs = LogCapture.getInstance().getLogs();
    }
    
    public void render(SpriteBatch batch) {
        if (!visible) return;
        
        // Draw semi-transparent background using FIXED UI camera
        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.setProjectionMatrix(uiCamera.combined); // Use fixed UI camera!
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.05f, 0.05f, 0.15f, 0.95f);
        shapeRenderer.rect(windowX, windowY, windowWidth, windowHeight);
        shapeRenderer.end();
        
        // Draw border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(2);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(windowX, windowY, windowWidth, windowHeight);
        shapeRenderer.end();
        
        // Set batch to use fixed UI camera for text
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        
        // Draw title at top
        titleFont.setColor(Color.GOLD);
        float textY = windowY + 25;
        titleFont.draw(batch, "GAME LOGS (Press L to close)", windowX + 10, textY);
        
        // Draw separator line info
        font.setColor(Color.YELLOW);
        textY += 25;
        font.draw(batch, "Adapter Pattern: Console + File + UI Display", windowX + 10, textY);
        
        // Draw logs
        textY += 35;
        int linesToShow = Math.min(14, cachedLogs.size());
        int startIndex = Math.max(0, cachedLogs.size() - linesToShow);
        
        if (cachedLogs.isEmpty()) {
            font.setColor(Color.GRAY);
            font.draw(batch, "No logs yet. Try these actions:", windowX + 15, textY);
            textY += 22;
            font.draw(batch, "  - Press SPACE to shoot", windowX + 15, textY);
            textY += 22;
            font.draw(batch, "  - Press 1-3 to change weapons", windowX + 15, textY);
            textY += 22;
            font.draw(batch, "  - Press E to place spike", windowX + 15, textY);
            textY += 22;
            font.setColor(Color.CYAN);
            font.draw(batch, "Watch the adapter pattern work in real-time!", windowX + 15, textY);
        } else {
            for (int i = startIndex; i < cachedLogs.size(); i++) {
                String log = cachedLogs.get(i);
                
                // Color code by severity and source
                if (log.contains("[SERVER]")) {
                    font.setColor(Color.ORANGE);
                } else if (log.contains("[INFO]")) {
                    font.setColor(Color.CYAN);
                } else if (log.contains("[WARN]")) {
                    font.setColor(Color.YELLOW);
                } else if (log.contains("[DEBUG]")) {
                    font.setColor(Color.LIGHT_GRAY);
                } else {
                    font.setColor(Color.WHITE);
                }
                
                // Truncate if too long
                if (log.length() > 75) {
                    log = log.substring(0, 72) + "...";
                }
                
                font.draw(batch, log, windowX + 10, textY);
                textY += 20;
            }
        }
        
        // Draw footer at bottom
        font.setColor(Color.GREEN);
        float footerY = windowY + windowHeight - 15;
        font.draw(batch, "Total Logs: " + cachedLogs.size() + " | CLIENT + SERVER", windowX + 10, footerY);
        
        // End the batch
        batch.end();
    }
    
    public void dispose() {
        font.dispose();
        titleFont.dispose();
        shapeRenderer.dispose();
    }
}