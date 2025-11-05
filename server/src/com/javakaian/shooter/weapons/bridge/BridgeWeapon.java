package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Refined Abstraction (Base)
 * 
 * This abstract class is the core of the Bridge pattern abstraction.
 * It maintains a reference to a FiringMechanism (the implementation)
 * and delegates firing behavior to it. This separates the weapon type
 * from how it fires, allowing any weapon to use any firing mechanism.
 * 
 * The Bridge pattern decouples abstraction (weapon types) from 
 * implementation (firing mechanisms), allowing them to vary independently.
 */
public abstract class BridgeWeapon {
    
    // Bridge Pattern: This is the "bridge" - composition instead of inheritance
    protected FiringMechanism firingMechanism;
    
    protected String name;
    protected float damage;
    protected float range;
    protected float baseFireRate;
    protected int ammoCapacity;
    protected int currentAmmo;
    protected boolean isReloading;
    
    /**
     * Constructor requires a FiringMechanism to be injected.
     * This is the key to the Bridge pattern - composition over inheritance.
     */
    public BridgeWeapon(String name, FiringMechanism firingMechanism) {
        this.name = name;
        this.firingMechanism = firingMechanism;
        this.damage = 10.0f;
        this.range = 100.0f;
        this.baseFireRate = 1.0f;
        this.ammoCapacity = 30;
        this.currentAmmo = ammoCapacity;
        this.isReloading = false;
    }
    
    /**
     * Bridge Pattern: Switches the implementation at runtime.
     * This demonstrates the flexibility of the Bridge pattern - 
     * the firing mechanism can be changed dynamically.
     */
    public void switchFiringMode(FiringMechanism newMechanism) {
        System.out.println("\n=== SWITCHING FIRING MODE ===");
        System.out.println("OLD: " + this.firingMechanism.getDescription());
        this.firingMechanism = newMechanism;
        System.out.println("NEW: " + this.firingMechanism.getDescription());
        System.out.println("============================\n");
    }
    
    /**
     * Fire the weapon using the current firing mechanism.
     * This delegates to the FiringMechanism implementation.
     */
    public void fire() {
        if (isReloading) {
            System.out.println(name + " is reloading, cannot fire!");
            return;
        }
        
        if (currentAmmo <= 0) {
            System.out.println(name + " is out of ammo! Reload needed.");
            return;
        }
        
        // Bridge Pattern: Delegate to the implementation
        int projectilesFired = firingMechanism.startFire(name, damage);
        currentAmmo -= projectilesFired;
        
        if (currentAmmo < 0) {
            currentAmmo = 0;
        }
        
        System.out.println("Ammo remaining: " + currentAmmo + "/" + ammoCapacity);
    }
    
    /**
     * Stop firing (mainly for full-auto and charged mechanisms).
     */
    public void stopFiring() {
        firingMechanism.stopFire(name);
    }
    
    /**
     * Reload the weapon.
     */
    public void reload() {
        if (currentAmmo == ammoCapacity) {
            System.out.println(name + " is already fully loaded!");
            return;
        }
        
        System.out.println(name + " reloading...");
        isReloading = true;
        currentAmmo = ammoCapacity;
        isReloading = false;
        System.out.println(name + " reload complete! Ammo: " + currentAmmo + "/" + ammoCapacity);
    }
    
    /**
     * Get the effective fire rate (base rate * mechanism multiplier).
     */
    public float getEffectiveFireRate() {
        return baseFireRate * firingMechanism.getFireRateMultiplier();
    }
    
    /**
     * Get complete weapon information including firing mechanism.
     */
    public String getFullDescription() {
        return String.format("%s | %s | Damage: %.1f | Range: %.1f | Fire Rate: %.2f | Ammo: %d/%d",
                name, 
                firingMechanism.getDescription(), 
                damage, 
                range, 
                getEffectiveFireRate(),
                currentAmmo,
                ammoCapacity);
    }
    
    // Abstract method to be implemented by concrete weapons
    public abstract String getWeaponType();
    
    // Getters and setters
    public String getName() { return name; }
    public float getDamage() { return damage; }
    public float getRange() { return range; }
    public float getBaseFireRate() { return baseFireRate; }
    public int getAmmoCapacity() { return ammoCapacity; }
    public int getCurrentAmmo() { return currentAmmo; }
    public FiringMechanism getFiringMechanism() { return firingMechanism; }
    
    public void setDamage(float damage) { this.damage = damage; }
    public void setRange(float range) { this.range = range; }
    public void setBaseFireRate(float baseFireRate) { this.baseFireRate = baseFireRate; }
    public void setAmmoCapacity(int ammoCapacity) { 
        this.ammoCapacity = ammoCapacity;
        this.currentAmmo = ammoCapacity;
    }
}
