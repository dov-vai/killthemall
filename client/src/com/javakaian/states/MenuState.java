package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.ThemeFactory.Theme;
import com.javakaian.shooter.ThemeFactory.ThemeFactory;
import com.javakaian.shooter.input.MenuStateInput;
import com.javakaian.shooter.utils.*;

import com.javakaian.shooter.logger.IGameLogger;
import com.javakaian.shooter.logger.ConsoleGameLoggerAdapter;
import com.javakaian.shooter.logger.GameLogEntry;

public class MenuState extends State {

    private BitmapFont smallFont;
    private boolean darkMode = true;
    private Theme currentTheme;

    private IGameLogger gameLogger;


    public MenuState(StateController sc) {
        super(sc);
        ip = new MenuStateInput(this);

        gameLogger = new ConsoleGameLoggerAdapter();
        
        // Log menu state creation
        GameLogEntry menuEvent = new GameLogEntry(
            System.currentTimeMillis(),
            "MENU_OPENED",
            "Main menu opened",
            "INFO"
        );
        gameLogger.logEvent(menuEvent);

        applyTheme();
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void toggleDarkMode() {
        darkMode = !darkMode;
        applyTheme();

        GameLogEntry themeEvent = new GameLogEntry(
            System.currentTimeMillis(),
            "THEME_CHANGE",
            "Theme changed to " + (darkMode ? "Dark Mode" : "Light Mode"),
            "INFO"
        );
        gameLogger.logEvent(themeEvent);
    }

    private void applyTheme() {
        ThemeFactory factory = ThemeFactory.getFactory(darkMode);
        currentTheme = factory.createTheme();

        if (smallFont != null) smallFont.dispose();
        if (bitmapFont != null) bitmapFont.dispose();

        bitmapFont = GameUtils.generateBitmapFont(64, currentTheme.getTextColor());
        smallFont = GameUtils.generateBitmapFont(32, currentTheme.getTextColor());
    }


    @Override
    public void render() {
        Color bg = currentTheme.getBackgroundColor();
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        GameUtils.renderCenter("KillThemAll", sb, bitmapFont);
        GameUtils.renderCenter("Select dark-light mode with right arrow key", sb, smallFont, 0.4f);
        GameUtils.renderCenter("Mode: " + (darkMode ? "Dark" : "Light"), sb, smallFont, 0.45f);
        GameUtils.renderCenter("Space - Play", sb, smallFont, 0.5f);
        GameUtils.renderCenter("S - Statistics", sb, smallFont, 0.58f);
        GameUtils.renderCenter("A - Achievements", sb, smallFont, 0.62f);
        GameUtils.renderCenter("Q - Quit", sb, smallFont, 0.66f);
        sb.end();
    }

    @Override
    public void update(float deltaTime) {
        // handle mode switching in MenuStateInput
    }

    public void quit() {
        GameLogEntry exitEvent = new GameLogEntry(
            System.currentTimeMillis(),
            "APP_EXIT",
            "User quit the application from menu",
            "INFO"
        );
        gameLogger.logEvent(exitEvent);

        Gdx.app.exit();
    }

    @Override
    public void dispose() {
        smallFont.dispose();
    }

    public void restart() {
        PlayState playState = (PlayState) this.sc.getStateMap().get(StateEnum.PLAY_STATE.ordinal());
        playState.restart();
    }

    public Theme getTheme() {
        return currentTheme;
    }
}
