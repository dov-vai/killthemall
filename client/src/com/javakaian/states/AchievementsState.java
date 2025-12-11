package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.achievements.Achievement;
import com.javakaian.shooter.achievements.AchievementManager;
import com.javakaian.shooter.achievements.AchievementIterator;
import com.javakaian.shooter.input.AchievementsStateInput;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.Subsystems.TextAlignment;
import com.javakaian.shooter.utils.fonts.FontManager;

public class AchievementsState extends State {

    private final AchievementManager achievementManager;
    //private BitmapFont smallFont;
    private FontManager smallFontManager;

    public AchievementsState(StateController sc, AchievementManager achievementManager) {
        super(sc);
        this.achievementManager = achievementManager;
        //smallFont = GameManagerFacade.getInstance().generateBitmapFont(28, Color.WHITE);
        smallFontManager = new FontManager(28, Color.WHITE, "Warungasem.ttf", "Achievements-SmallFont");
        //smallFontManager.switchToSecurityProxy(false); // Restrict initially
        smallFontManager.switchToVirtualProxy();

        // check if player has unlocked achievements screen
        // boolean hasAccess = true;
        // smallFontManager.setSecurityAccess(hasAccess);
        ip = new AchievementsStateInput(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameManagerFacade gm = GameManagerFacade.getInstance();

        BitmapFont smallFont = smallFontManager.getFontResource().getFont();
        if (smallFont == null) {
            smallFont = bitmapFont; // fallback to main font
        }

        sb.begin();

        gm.renderText(sb, bitmapFont, "Achievements", TextAlignment.CENTER, 0f, 0.15f);

        // Iterator Pattern - Use iterator to traverse unlocked achievements
        AchievementIterator iterator = achievementManager.createUnlockedIterator();
        
        if (!iterator.hasNext()) {
            gm.renderText(sb, smallFont, "No achievements yet", TextAlignment.CENTER, 0f, 0.30f);
        } else {
            float y = 0.28f;
            while (iterator.hasNext() && y <= 0.85f) {
                Achievement a = iterator.next();
                gm.renderText(sb, smallFont, a.getTitle() + " - " + a.getDescription(), TextAlignment.CENTER, 0f, y);
                y += 0.06f;
            }
        }

        gm.renderText(sb, smallFont, "ESC - Back", TextAlignment.CENTER, 0f, 0.9f);

        sb.end();
    }

    @Override
    public void update(float deltaTime) {
        // no-op
    }

    @Override
    public void dispose() {
        // no-op
        if (smallFontManager != null) {
            smallFontManager.dispose();
        }
    }

    public void backToMenu() {
        sc.setState(StateEnum.MENU_STATE);
    }
    
    /**
     * Run the Iterator Pattern demonstration
     */
    public void runIteratorDemo() {
        com.javakaian.shooter.achievements.AchievementIteratorDemo.runDemo(achievementManager);
    }
}
