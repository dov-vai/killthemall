package com.javakaian.shooter.memento;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.weapons.Weapon;

/**
 * Memento class that stores Player state.
 * This class implements IMemento (narrow interface) but also provides
 * public getter methods that allow the Originator (Player) to restore state.
 * 
 * SECURITY: Although getters are public, the Caretaker only sees IMemento interface
 * (which has no methods), so it cannot access the state. Only Player knows to cast
 * IMemento back to PlayerMemento.
 * 
 * This is the "Wide Interface" - only the Originator (Player) has the knowledge
 * to cast and access all the state data for restoration.
 */
public class PlayerMemento implements IMemento {
    
    // Stored state - all fields are private for encapsulation
    private final Vector2 position;
    private final int health;
    private final boolean alive;
    private final Weapon currentWeapon;
    private final int spikeCount;
    
    // Power-up states
    private final boolean hasSpeedBoost;
    private final float speedBoostEndTime;
    private final float speedMultiplier;
    
    private final boolean hasDamageBoost;
    private final float damageBoostEndTime;
    private final float damageMultiplier;
    
    private final boolean hasShield;
    private final float shieldEndTime;
    private final int shieldHealth;
    
    private final float lastShotTime;
    
    // Timestamp for debugging
    private final long timestamp;
    
    /**
     * Constructor - public so Player (Originator) can create mementos.
     * Security is maintained because only Player knows WHEN and HOW to create mementos.
     */
    public PlayerMemento(Vector2 position, int health, boolean alive, Weapon currentWeapon,
                         int spikeCount, boolean hasSpeedBoost, float speedBoostEndTime,
                         float speedMultiplier, boolean hasDamageBoost, float damageBoostEndTime,
                         float damageMultiplier, boolean hasShield, float shieldEndTime,
                         int shieldHealth, float lastShotTime) {
        // Create a copy of position to avoid external modifications
        this.position = new Vector2(position.x, position.y);
        this.health = health;
        this.alive = alive;
        this.currentWeapon = currentWeapon;
        this.spikeCount = spikeCount;
        this.hasSpeedBoost = hasSpeedBoost;
        this.speedBoostEndTime = speedBoostEndTime;
        this.speedMultiplier = speedMultiplier;
        this.hasDamageBoost = hasDamageBoost;
        this.damageBoostEndTime = damageBoostEndTime;
        this.damageMultiplier = damageMultiplier;
        this.hasShield = hasShield;
        this.shieldEndTime = shieldEndTime;
        this.shieldHealth = shieldHealth;
        this.lastShotTime = lastShotTime;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Public getters - accessible to Player (Originator) for restoration
    // This is the "Wide Interface" that provides full access to the state
    // Security: Caretaker only sees IMemento (no getters), so cannot access these
    
    public Vector2 getPosition() {
        return new Vector2(position.x, position.y); // Return copy for safety
    }
    
    public int getHealth() {
        return health;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }
    
    public int getSpikeCount() {
        return spikeCount;
    }
    
    public boolean hasSpeedBoost() {
        return hasSpeedBoost;
    }
    
    public float getSpeedBoostEndTime() {
        return speedBoostEndTime;
    }
    
    public float getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    public boolean hasDamageBoost() {
        return hasDamageBoost;
    }
    
    public float getDamageBoostEndTime() {
        return damageBoostEndTime;
    }
    
    public float getDamageMultiplier() {
        return damageMultiplier;
    }
    
    public boolean hasShield() {
        return hasShield;
    }
    
    public float getShieldEndTime() {
        return shieldEndTime;
    }
    
    public int getShieldHealth() {
        return shieldHealth;
    }
    
    public float getLastShotTime() {
        return lastShotTime;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "PlayerMemento{" +
                "position=" + position +
                ", health=" + health +
                ", alive=" + alive +
                ", spikeCount=" + spikeCount +
                ", speedBoost=" + hasSpeedBoost +
                ", damageBoost=" + hasDamageBoost +
                ", shield=" + hasShield +
                ", timestamp=" + timestamp +
                '}';
    }
}