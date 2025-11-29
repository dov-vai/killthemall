package com.javakaian.shooter.mediator;

/**
 * Enum representing different types of collision-related events
 * that game entities can notify the mediator about.
 */
public enum CollisionEvent {
    MOVED,          // Entity has moved and may need collision checking
    PICKUP_READY,   // Pickup item is ready to be collected
    SPIKE_PLACED    // Spike has been placed on the ground
}
