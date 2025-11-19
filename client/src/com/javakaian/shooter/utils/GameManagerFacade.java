package com.javakaian.shooter.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.List;

import com.javakaian.shooter.shapes.AimLine;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.utils.Subsystems.*;
import com.javakaian.shooter.utils.stats.GameStats;

public class GameManagerFacade {

    private static GameManagerFacade instance;

    private final ObjectRenderSystem objectRenderSystem;
    private final GameStats stats;

    private GameManagerFacade() {
        objectRenderSystem = new ObjectRenderSystem();
        stats = GameStats.getInstance();
    }

    public static synchronized GameManagerFacade getInstance() {
        if (instance == null) {
            instance = new GameManagerFacade();
        }
        return instance;
    }

    public void renderGameObjects(ShapeRenderer sr,
                                  List<Player> otherPlayers,
                                  List<?> enemies,
                                  List<?> bullets,
                                  List<?> spikes,
                                  List<?> placedSpikes,
                                  List<?> powerUps,
                                  Player mainPlayer,
                                  AimLine aimLine) {
        objectRenderSystem.renderObjects(sr, otherPlayers, enemies, bullets, spikes, placedSpikes, powerUps, mainPlayer, aimLine);
    }

    public void renderText(SpriteBatch sb, BitmapFont font, String text, TextAlignment alignment, float x, float y) {
        if (sb == null || font == null || text == null) return;

        switch (alignment) {
            case CENTER:
                GameUtils.renderCenter(text, sb, font, y);
                break;
            case LEFT:
                GameUtils.renderLeftAligned(text, sb, font, x, y);
                break;
            case RIGHT:
                throw new UnsupportedOperationException("render right not impelemnted yet");
        }
    }
    public void startSession() {
        stats.resetSession();
        stats.startSession();
    }

    public void endSession() {
        stats.endSession();
    }

    public float stats(StatAction action, StatType type, float... value) {
        switch (action) {
            case GET -> {
                return switch (type) {
                    case TOTAL_SESSIONS -> stats.getTotalSessions();
                    case BEST_TIME -> stats.getBestTimeSeconds();
                    case TOTAL_DEATHS -> stats.getTotalDeaths();
                    case TOTAL_SHOTS -> stats.getTotalShots();
                    case TOTAL_DAMAGE -> stats.getTotalDamage();
                    case TOTAL_DISTANCE -> stats.getTotalDistance();
                };
            }
            case SET -> {
                float v = value.length > 0 ? value[0] : 0f;
                switch (type) {
                    case TOTAL_DEATHS -> stats.incrementDeaths();
                    case TOTAL_SHOTS -> stats.incrementShotsFired();
                    case TOTAL_DAMAGE -> stats.addDamageTaken(value[0]);
                }
                return 0;
            }
        }
        return 0;
    }

    public BitmapFont generateBitmapFont(int size, Color color) {
        return GameUtils.generateBitmapFont(size, color);
    }



}

