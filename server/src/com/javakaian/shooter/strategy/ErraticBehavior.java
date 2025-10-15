package com.javakaian.shooter.strategy;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

import java.security.SecureRandom;
import java.util.List;

/**
 * Erratic behavior - random unpredictable movements.
 * This strategy makes enemies move in random directions, occasionally towards players.
 */
public class ErraticBehavior implements EnemyBehaviorStrategy {
    
    private static final float SPEED = 80f;
    private Vector2 currentDirection;
    private float directionChangeTimer = 0f;
    private float directionChangeCooldown = 1.5f; // Change direction every 1.5 seconds
    private SecureRandom random;
    
    public ErraticBehavior() {
        this.random = new SecureRandom();
        this.currentDirection = randomDirection();
    }
    
    @Override
    public Vector2 behaveDifferently(Vector2 currentPosition, List<Player> players, float deltaTime) {
        directionChangeTimer += deltaTime;
        
        // Randomly change direction
        if (directionChangeTimer >= directionChangeCooldown) {
            directionChangeTimer = 0f;
            directionChangeCooldown = 1f + random.nextFloat() * 2f; // Random between 1-3 seconds
            
            // 30% chance to move towards a player, 70% chance random
            if (!players.isEmpty() && random.nextFloat() < 0.3f) {
                Player randomPlayer = players.get(random.nextInt(players.size()));
                currentDirection = new Vector2(randomPlayer.getPosition()).sub(currentPosition).nor();
            } else {
                currentDirection = randomDirection();
            }
        }
        
        // Move in current direction
        currentPosition.add(currentDirection.cpy().scl(SPEED * deltaTime));
        
        return currentPosition;
    }
    
    private Vector2 randomDirection() {
        float angle = random.nextFloat() * (float) Math.PI * 2;
        return new Vector2((float) Math.cos(angle), (float) Math.sin(angle));
    }
    
    @Override
    public String getStrategyName() {
        return "Erratic";
    }
}
