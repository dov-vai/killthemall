package com.javakaian.network.messages;

/**
 * Message sent from server to client to update the player's power-up inventory display
 */
public class PowerUpInventoryMessage {
    private int playerId;
    // Slots 0-3 contain power-up type ordinals (-1 if empty)
    private int[] slots = new int[4];

    public PowerUpInventoryMessage() {
        for (int i = 0; i < 4; i++) {
            slots[i] = -1; // -1 means empty slot
        }
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int[] getSlots() {
        return slots;
    }

    public void setSlots(int[] slots) {
        this.slots = slots;
    }

    public void setSlot(int index, int powerUpType) {
        if (index >= 0 && index < 4) {
            slots[index] = powerUpType;
        }
    }

    public int getSlot(int index) {
        if (index >= 0 && index < 4) {
            return slots[index];
        }
        return -1;
    }
}
