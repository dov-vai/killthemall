package com.javakaian.shooter.shapes;

import java.util.List;

public class UpdateContext {
    public final float deltaTime;
    public final List<Player> players;

    public UpdateContext(float deltaTime, List<Player> players) {
        this.deltaTime = deltaTime;
        this.players = players;
    }
}
