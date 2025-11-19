package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Implementation Interface
 * <p>
 * This is the implementor interface in the Bridge pattern.
 * It defines the interface for firing mechanisms that can be
 * used by different weapons. This separates the firing behavior
 * from the weapon abstraction.
 */
public interface FiringMechanism {

    /**
     * Initiates the firing sequence.
     *
     * @param weaponName The name of the weapon firing
     * @param damage     The damage value of the weapon
     * @return Number of projectiles fired
     */
    int startFire(String weaponName, float damage);

    /**
     * Stops the firing sequence.
     *
     * @param weaponName The name of the weapon
     */
    void stopFire(String weaponName);

    /**
     * Gets the firing mode description.
     *
     * @return Description of the firing mechanism
     */
    String getDescription();

    /**
     * Gets the fire rate multiplier for this mechanism.
     *
     * @return Fire rate multiplier
     */
    float getFireRateMultiplier();
}
