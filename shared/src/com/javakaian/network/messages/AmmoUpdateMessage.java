package com.javakaian.network.messages;

public class AmmoUpdateMessage {
    private int playerId;
    private int currentAmmo;
    private int ammoCapacity;

    public AmmoUpdateMessage() {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    public int getAmmoCapacity() {
        return ammoCapacity;
    }

    public void setAmmoCapacity(int ammoCapacity) {
        this.ammoCapacity = ammoCapacity;
    }
}

