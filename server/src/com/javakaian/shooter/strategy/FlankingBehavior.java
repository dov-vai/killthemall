package com.javakaian.shooter.strategy;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

import java.util.List;

/**
 * Flanking behavior - tries to circle around players and attack from sides.
 * This strategy makes enemies move in a circular pattern around their target.
 */
public class FlankingBehavior implements EnemyBehaviorStrategy {

    private static final float SPEED = 120f;
    private static final float ORBIT_DISTANCE = 200f;
    private float angle = 0f;

    @Override
    public Vector2 behaveDifferently(Vector2 currentPosition, List<Player> players, float deltaTime) {
        if (players.isEmpty()) {
            return currentPosition;
        }

        // Find nearest player to flank
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
            Vector2 toPlayer = new Vector2(nearestPlayer.getPosition()).sub(currentPosition);
            float currentDistance = toPlayer.len();

            // Adjust angle for circular movement
            angle += deltaTime * 2f; // 2 radians per second

            // Calculate tangent vector (perpendicular to direction to player)
            Vector2 tangent = new Vector2(-toPlayer.y, toPlayer.x).nor();

            // Mix of moving towards orbit distance and circling
            Vector2 movement = new Vector2();

            if (currentDistance > ORBIT_DISTANCE + 50) {
                // Too far, move closer
                movement.add(toPlayer.nor().scl(0.7f));
                movement.add(tangent.scl(0.3f));
            } else if (currentDistance < ORBIT_DISTANCE - 50) {
                // Too close, move away
                movement.add(toPlayer.nor().scl(-0.3f));
                movement.add(tangent.scl(0.7f));
            } else {
                // At good distance, just circle
                movement.add(tangent);
            }

            currentPosition.add(movement.nor().scl(SPEED * deltaTime));
        }

        return currentPosition;
    }

    @Override
    public String getStrategyName() {
        return "Flanking";
    }
}
