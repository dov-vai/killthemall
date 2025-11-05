package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.achievements.Achievement;
import com.javakaian.shooter.achievements.AchievementManager;
import com.javakaian.shooter.input.AchievementsStateInput;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.GameUtils;

import java.util.List;

public class AchievementsState extends State {

    private final AchievementManager achievementManager;
    private BitmapFont smallFont;

    public AchievementsState(StateController sc, AchievementManager achievementManager) {
        super(sc);
        this.achievementManager = achievementManager;
        smallFont = GameManagerFacade.getInstance().generateBitmapFont(28, Color.WHITE);
        ip = new AchievementsStateInput(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        GameUtils.renderCenter("Achievements", sb, bitmapFont, 0.15f);
        List<Achievement> unlocked = achievementManager.getUnlocked();
        if (unlocked.isEmpty()) {
            GameUtils.renderCenter("No achievements yet", sb, smallFont, 0.30f);
        } else {
            float y = 0.28f;
            for (Achievement a : unlocked) {
                GameUtils.renderCenter(a.getTitle() + " - " + a.getDescription(), sb, smallFont, y);
                y += 0.06f;
                if (y > 0.85f) break; // avoid overflow
            }
        }
        GameUtils.renderCenter("ESC - Back", sb, smallFont, 0.9f);
        sb.end();
    }

    @Override
    public void update(float deltaTime) {
        // no-op
    }

    @Override
    public void dispose() {
        // no-op
    }

    public void backToMenu() {
        sc.setState(StateEnum.MENU_STATE);
    }
}
