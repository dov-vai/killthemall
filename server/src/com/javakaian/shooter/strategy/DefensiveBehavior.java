package com.javakaian.shooter.strategy;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

import java.util.List;

/**
 * Defensive behavior - tries to maintain distance from players.
 * This strategy makes enemies avoid players and move away when they get too close.
 */
public class DefensiveBehavior implements EnemyBehaviorStrategy {
    
    private static final float SPEED = 100f;
    private static final float SAFE_DISTANCE = 300f;
    
    @Override
    public Vector2 behaveDifferently(Vector2 currentPosition, List<Player> players, float deltaTime) {
        if (players.isEmpty()) {
            return currentPosition;
        }
        
        // Calculate repulsion vector from all nearby players
        Vector2 repulsion = new Vector2(0, 0);
        
        for (Player player : players) {
            float distance = currentPosition.dst(player.getPosition());
            
            // If player is too close, move away
            if (distance < SAFE_DISTANCE && distance > 0) {
                Vector2 awayDirection = new Vector2(currentPosition).sub(player.getPosition()).nor();
                float repulsionStrength = (SAFE_DISTANCE - distance) / SAFE_DISTANCE;
                repulsion.add(awayDirection.scl(repulsionStrength));
            }
        }
        
        if (repulsion.len() > 0) {
            repulsion.nor();
            currentPosition.add(repulsion.scl(SPEED * deltaTime));
        }
        
        return currentPosition;
    }
    
    @Override
    public String getStrategyName() {
        return "Defensive";
    }
}
