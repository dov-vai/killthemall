package com.javakaian.shooter.mediator;

/**
 * Mediator interface for handling collision detection in the game.
 * Game entities notify the mediator when they move or change state,
 * and the mediator coordinates collision checking between all entities.
 */
public interface CollisionMediator {
    
    /**
     * Notify the mediator that an entity has changed state or moved.
     * The mediator will check for collisions involving this entity.
     * 
     * @param sender The object that triggered the notification
     * @param event The type of event that occurred
     */
    void notify(Object sender, CollisionEvent event);
    
    /**
     * Update the game time for power-up effects and time-based logic.
     * 
     * @param gameTime The current game time in seconds
     */
    void setGameTime(float gameTime);
}
