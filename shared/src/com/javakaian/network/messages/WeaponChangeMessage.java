package com.javakaian.network.messages;

public class WeaponChangeMessage {
    private int playerId;
    private String weaponConfig;
    
    public WeaponChangeMessage() {}
    
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    
    public String getWeaponConfig() { return weaponConfig; }
    public void setWeaponConfig(String weaponConfig) { this.weaponConfig = weaponConfig; }
}