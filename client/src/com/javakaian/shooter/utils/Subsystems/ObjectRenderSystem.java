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
                              Player mainPlayer,
                              AimLine aimLine) {

        sr.begin(ShapeType.Line);

        if (otherPlayers != null) {
            sr.setColor(Color.RED);
            for (Player p : otherPlayers) {
                if (p != null) p.render(sr);
            }
        }

        List<?>[] otherLists = new List<?>[]{enemies, bullets, spikes, placedSpikes};
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
        }

        if (aimLine != null) aimLine.render(sr);

        sr.end();
    }

}
