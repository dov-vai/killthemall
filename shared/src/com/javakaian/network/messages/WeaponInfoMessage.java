package com.javakaian.network.messages;

public class WeaponInfoMessage {
    private int playerId;
    private String weaponName;
    private String components;
    private String stats;
    private float fireRate;
    private float bulletSize;
    
    public WeaponInfoMessage() {}
    
    // Getters and setters
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    
    public String getWeaponName() { return weaponName; }
    public void setWeaponName(String weaponName) { this.weaponName = weaponName; }
    
    public String getComponents() { return components; }
    public void setComponents(String components) { this.components = components; }
    
    public String getStats() { return stats; }
    public void setStats(String stats) { this.stats = stats; }
    
    public float getFireRate() { return fireRate; }
    public void setFireRate(float fireRate) { this.fireRate = fireRate; }
    
    public float getBulletSize() { return bulletSize; }
    public void setBulletSize(float bulletSize) { this.bulletSize = bulletSize; }
}