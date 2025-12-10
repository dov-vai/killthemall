package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import org.apache.log4j.Logger;

public class RealFontResource implements FontResource {
    
    private static final Logger logger = Logger.getLogger(RealFontResource.class);
    
    private final int size;
    private final Color color;
    private final String fontPath;
    private final String identifier;
    private BitmapFont font;
    private boolean loaded = false;
    
    public RealFontResource(int size, Color color, String fontPath, String identifier) {
        this.size = size;
        this.color = color;
        this.fontPath = fontPath;
        this.identifier = identifier;
        createFont(); // Creates immediately
    }
    
    private void createFont() {
        long startTime = System.currentTimeMillis();
        
        logger.info(String.format("[%s] RealFontResource: Creating font (size=%d)...", identifier, size));
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.flip = true;
        parameter.size = 2 * size;
        parameter.color = color;
        parameter.magFilter = TextureFilter.Linear;
        parameter.minFilter = TextureFilter.Linear;
        
        font = generator.generateFont(parameter);
        generator.dispose();
        loaded = true;
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info(String.format("[%s] RealFontResource: Font created in %dms", identifier, duration));
    }
    
    @Override
    public BitmapFont getFont() {
        return font;
    }
    
    @Override
    public void dispose() {
        if (font != null) {
            logger.info(String.format("[%s] RealFontResource: Disposing font", identifier));
            font.dispose();
            loaded = false;
        }
    }
    
    @Override
    public boolean isLoaded() {
        return loaded;
    }
}