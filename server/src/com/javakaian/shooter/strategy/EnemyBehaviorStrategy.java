package com.javakaian.shooter.strategy;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

import java.util.List;

/**
 * Strategy interface for enemy behavior.
 * Defines the contract for different enemy AI behaviors.
 */
public interface EnemyBehaviorStrategy {

    /**
     * Calculates the next position for the enemy based on its behavior strategy.
     *
     * @param currentPosition The current position of the enemy
     * @param players         List of all active players
     * @param deltaTime       Time elapsed since last update
     * @return The new position vector for the enemy
     */
    Vector2 behaveDifferently(Vector2 currentPosition, List<Player> players, float deltaTime);

    /**
     * Gets the name of this behavior strategy.
     *
     * @return Strategy name
     */
    String getStrategyName();
}
