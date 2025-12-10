package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.ThemeFactory.Theme;
import com.javakaian.shooter.ThemeFactory.ThemeFactory;
import com.javakaian.shooter.input.MenuStateInput;
import com.javakaian.shooter.logger.ConsoleGameLoggerAdapter;
import com.javakaian.shooter.logger.GameLogEntry;
import com.javakaian.shooter.logger.IGameLogger;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.Subsystems.TextAlignment;
import com.javakaian.shooter.utils.fonts.FontManager;

public class MenuState extends State {

    private BitmapFont smallFont;
    private boolean darkMode = true;
    private Theme currentTheme;

    private IGameLogger gameLogger;

    private FontManager bitmapFontManager;
    private FontManager smallFontManager;

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

        // if (smallFont != null) smallFont.dispose();
        // if (bitmapFont != null) bitmapFont.dispose();

        // bitmapFont = GameManagerFacade.getInstance().generateBitmapFont(64, currentTheme.getTextColor());
        // smallFont = GameManagerFacade.getInstance().generateBitmapFont(32, currentTheme.getTextColor());

        if (bitmapFontManager == null) {
            bitmapFontManager = new FontManager(64, currentTheme.getTextColor(), "Warungasem.ttf", "Menu-BitmapFont");
            smallFontManager = new FontManager(32, currentTheme.getTextColor(), "Warungasem.ttf", "Menu-SmallFont");
            
            // Use Virtual Proxy for delayed creation
            bitmapFontManager.switchToVirtualProxy();
            smallFontManager.switchToVirtualProxy();
        }

        bitmapFont = bitmapFontManager.getFontResource().getFont();
        smallFont = smallFontManager.getFontResource().getFont();
        
    }


    @Override
    public void render() {
        Color bg = currentTheme.getBackgroundColor();
        Gdx.gl.glClearColor(bg.r, bg.g, bg.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameManagerFacade gm = GameManagerFacade.getInstance();

        sb.begin();
        gm.renderText(sb, bitmapFont, "KillThemAll", TextAlignment.CENTER, 0f, 0.3f);
        gm.renderText(sb, smallFont, "Select dark-light mode with right arrow key", TextAlignment.CENTER, 0f, 0.4f);
        gm.renderText(sb, smallFont, "Mode: " + (darkMode ? "Dark" : "Light"), TextAlignment.CENTER, 0f, 0.45f);
        gm.renderText(sb, smallFont, "Space - Play", TextAlignment.CENTER, 0f, 0.5f);
        gm.renderText(sb, smallFont, "S - Statistics", TextAlignment.CENTER, 0f, 0.58f);
        gm.renderText(sb, smallFont, "A - Achievements", TextAlignment.CENTER, 0f, 0.62f);
        gm.renderText(sb, smallFont, "Q - Quit", TextAlignment.CENTER, 0f, 0.66f);
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
        //smallFont.dispose();
        bitmapFontManager.dispose();
        smallFontManager.dispose();
    }

    public void restart() {
        PlayState playState = (PlayState) this.sc.getStateMap().get(StateEnum.PLAY_STATE.ordinal());
        playState.restart();
    }

    public Theme getTheme() {
        return currentTheme;
    }
}
