package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.input.GameOverInput;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.Subsystems.TextAlignment;
import com.javakaian.shooter.utils.stats.GameStats;

/**
 * This class controls game over state.
 *
 * @author oguz
 */
public class GameOverState extends State {

    private BitmapFont smallFont;

    public GameOverState(StateController sc) {
        super(sc);
        ip = new GameOverInput(this);
        smallFont = GameManagerFacade.getInstance().generateBitmapFont(32, Color.WHITE);

    }

    @Override
    public void render() {
        float red = 50f;
        float green = 63f;
        float blue = 94f;
        Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GameManagerFacade gm = GameManagerFacade.getInstance();

        sb.begin();
        gm.renderText(sb, bitmapFont, "Game Over", TextAlignment.CENTER, 0f, 0.45f);
        gm.renderText(sb, smallFont,
                "Time Alive: " + String.format("%.1fs", GameStats.getInstance().getTimeAliveSeconds()),
                TextAlignment.CENTER, 0f, 0.52f);
        gm.renderText(sb, smallFont, "Press R to Restart", TextAlignment.CENTER, 0f, 0.6f);
        gm.renderText(sb, smallFont, "M - Main Menu", TextAlignment.CENTER, 0f, 0.68f);
        sb.end();
    }


    @Override
    public void update(float deltaTime) {
        // do update things here
    }

    @Override
    public void dispose() {
        // do disposes here
    }

    public void restart() {
        PlayState playState = (PlayState) this.sc.getStateMap().get(StateEnum.PLAY_STATE.ordinal());
        playState.restart();
    }

}
