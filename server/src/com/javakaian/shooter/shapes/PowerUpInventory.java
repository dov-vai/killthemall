package com.javakaian.shooter.shapes;

import com.javakaian.shooter.shapes.PowerUp.PowerUpType;

/**
 * Inventory system for storing collected power-ups.
 * Players can store up to 4 power-ups and use them later with F1-F4 keys.
 */
public class PowerUpInventory {
    
    public static final int MAX_SLOTS = 4;
    
    // Each slot can hold one power-up type, -1 means empty
    private PowerUpType[] slots;
    private float[] durations; // Duration for each stored power-up
    
    public PowerUpInventory() {
        slots = new PowerUpType[MAX_SLOTS];
        durations = new float[MAX_SLOTS];
        clear();
    }
    
    /**
     * Attempts to add a power-up to the inventory.
     * @return true if added successfully, false if inventory is full
     */
    public boolean addPowerUp(PowerUpType type, float duration) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (slots[i] == null) {
                slots[i] = type;
                durations[i] = duration;
                return true;
            }
        }
        return false; // Inventory full
    }
    
    /**
     * Uses a power-up from the specified slot.
     * @param slot The slot index (0-3)
     * @return The power-up type and duration, or null if slot is empty
     */
    public PowerUpData usePowerUp(int slot) {
        if (slot < 0 || slot >= MAX_SLOTS) {
            return null;
        }
        
        PowerUpType type = slots[slot];
        float duration = durations[slot];
        
        if (type != null) {
            slots[slot] = null;
            durations[slot] = 0;
            return new PowerUpData(type, duration);
        }
        
        return null;
    }
    
    /**
     * Gets the power-up type at a specific slot.
     * @return The type or null if empty
     */
    public PowerUpType getSlot(int index) {
        if (index >= 0 && index < MAX_SLOTS) {
            return slots[index];
        }
        return null;
    }
    
    /**
     * Gets the slot ordinals for network transmission.
     * Returns -1 for empty slots, otherwise the PowerUpType ordinal.
     */
    public int[] getSlotsAsOrdinals() {
        int[] ordinals = new int[MAX_SLOTS];
        for (int i = 0; i < MAX_SLOTS; i++) {
            ordinals[i] = slots[i] != null ? slots[i].ordinal() : -1;
        }
        return ordinals;
    }
    
    /**
     * Checks if the inventory is full.
     */
    public boolean isFull() {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (slots[i] == null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the inventory is empty.
     */
    public boolean isEmpty() {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (slots[i] != null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Clears the inventory.
     */
    public void clear() {
        for (int i = 0; i < MAX_SLOTS; i++) {
            slots[i] = null;
            durations[i] = 0;
        }
    }
    
    /**
     * Gets the count of stored power-ups.
     */
    public int getCount() {
        int count = 0;
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (slots[i] != null) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Data class to hold power-up type and duration when using.
     */
    public static class PowerUpData {
        public final PowerUpType type;
        public final float duration;
        
        public PowerUpData(PowerUpType type, float duration) {
            this.type = type;
            this.duration = duration;
        }
    }
}
