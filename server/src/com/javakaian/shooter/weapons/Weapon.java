package com.javakaian.shooter.weapons;

public abstract class Weapon {
    protected String barrel;
    protected String scope;
    protected String stock;
    protected String magazine;
    protected String grip;
    protected String name;
    protected float damage;
    protected float range;
    protected float fireRate;
    protected int ammoCapacity;

    public Weapon(String name) {
        this.name = name;
        this.damage = 10.0f;
        this.range = 100.0f;
        this.fireRate = 1.0f;
        this.ammoCapacity = 30;
    }

    // Getters and setters
    public void setBarrel(String barrel) { this.barrel = barrel; }
    public void setScope(String scope) { this.scope = scope; }
    public void setStock(String stock) { this.stock = stock; }
    public void setMagazine(String magazine) { this.magazine = magazine; }
    public void setGrip(String grip) { this.grip = grip; }
    public void setDamage(float damage) { this.damage = damage; }
    public void setRange(float range) { this.range = range; }
    public void setFireRate(float fireRate) { this.fireRate = fireRate; }
    public void setAmmoCapacity(int ammoCapacity) { this.ammoCapacity = ammoCapacity; }

    public String getName() { return name; }
    public float getDamage() { return damage; }
    public float getRange() { return range; }
    public float getFireRate() { return fireRate; }
    public int getAmmoCapacity() { return ammoCapacity; }
    public String getBarrel() { return barrel; }
    public String getScope() { return scope; }
    public String getStock() { return stock; }
    public String getMagazine() { return magazine; }
    public String getGrip() { return grip; }

    public abstract void shoot();
    public abstract String getDescription();
}