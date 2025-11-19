package com.javakaian.shooter.utils.Subsystems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.javakaian.shooter.shapes.*;

import java.util.List;

public class ObjectRenderSystem {

    public void renderObjects(ShapeRenderer sr,
                              List<Player> otherPlayers,
                              List<?> enemies,
                              List<?> bullets,
                              List<?> spikes,
                              List<?> placedSpikes,
                              List<?> powerUps,
                              Player mainPlayer,
                              AimLine aimLine) {

        sr.begin(ShapeType.Line);

        if (otherPlayers != null) {
            sr.setColor(Color.RED);
            for (Player p : otherPlayers) {
                if (p != null) {
                    p.render(sr);
                    
                    if (p.hasShield()) {
                        sr.setColor(new Color(0, 1, 1, 0.5f));  // Cyan
                        float centerX = p.getPosition().x + 25;
                        float centerY = p.getPosition().y + 25;
                        sr.circle(centerX, centerY, 35);
                        sr.circle(centerX, centerY, 38);
                        sr.setColor(Color.RED);
                    }
                }
            }
        }

        List<?>[] otherLists = new List<?>[]{enemies, bullets, spikes, placedSpikes, powerUps};
        for (List<?> list : otherLists) {
            if (list == null) continue;
            for (Object obj : list) {
                if (obj == null) continue;
                try {
                    obj.getClass().getMethod("render", ShapeRenderer.class).invoke(obj, sr);
                } catch (Exception ignored) {}
            }
        }

        if (mainPlayer != null) {
            sr.setColor(Color.BLUE);
            mainPlayer.render(sr);

            if (mainPlayer.hasShield()) {
                sr.setColor(new Color(0, 1, 1, 0.5f));
                float centerX = mainPlayer.getPosition().x + 25;
                float centerY = mainPlayer.getPosition().y + 25;
                sr.circle(centerX, centerY, 35);
                sr.circle(centerX, centerY, 38);
            }
        }

        if (aimLine != null) aimLine.render(sr);

        sr.end();
    }

}
