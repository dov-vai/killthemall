package com.javakaian.shooter.strategy;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

import java.util.List;

/**
 * Aggressive behavior - chases the nearest player directly.
 * This strategy makes enemies move straight towards the closest player.
 */
public class AggressiveBehavior implements EnemyBehaviorStrategy {
    
    private static final float SPEED = 150f;
    
    @Override
    public Vector2 behaveDifferently(Vector2 currentPosition, List<Player> players, float deltaTime) {
        if (players.isEmpty()) {
            return currentPosition;
        }
        
        // Find nearest player
        Player nearestPlayer = null;
        float minDistance = Float.MAX_VALUE;
        
        for (Player player : players) {
            float distance = currentPosition.dst(player.getPosition());
            if (distance < minDistance) {
                minDistance = distance;
                nearestPlayer = player;
            }
        }
        
        if (nearestPlayer != null) {
            // Move towards nearest player
            Vector2 direction = new Vector2(nearestPlayer.getPosition()).sub(currentPosition).nor();
            currentPosition.add(direction.scl(SPEED * deltaTime));
        }
        
        return currentPosition;
    }
    
    @Override
    public String getStrategyName() {
        return "Aggressive";
    }
}
