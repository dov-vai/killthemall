package com.javakaian.shooter.utils.fonts;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface FontResource {
    BitmapFont getFont();
    void dispose();
    boolean isLoaded();
}