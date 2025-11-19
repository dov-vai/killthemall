package com.javakaian.shooter.weapons;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;

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
    protected int currentAmmo;
    protected float reloadDuration = 1.6f;
    protected boolean reloading = false;
    protected float reloadFinishTime = 0f;

    public Weapon(String name) {
        this.name = name;
        this.damage = 10.0f;
        this.range = 100.0f;
        this.fireRate = 1.0f;
        this.ammoCapacity = 30;
        this.currentAmmo = this.ammoCapacity;
    }

    public String getName() {
        return name;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public int getAmmoCapacity() {
        return ammoCapacity;
    }

    public void setAmmoCapacity(int ammoCapacity) {
        this.ammoCapacity = ammoCapacity;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmo(int currentAmmo) {
        this.currentAmmo = Math.max(0, Math.min(currentAmmo, ammoCapacity));
    }

    public boolean isReloading(float now) {
        return reloading && now < reloadFinishTime;
    }

    public boolean needsReload() {
        return currentAmmo < ammoCapacity;
    }

    public void startReload(float now) {
        if (reloading || currentAmmo >= ammoCapacity)
            return;
        reloading = true;
        reloadFinishTime = now + reloadDuration;
    }

    public boolean tryFinishReload(float now) {
        if (reloading && now >= reloadFinishTime) {
            currentAmmo = ammoCapacity;
            reloading = false;
            return true;
        }
        return false;
    }

    public String getBarrel() {
        return barrel;
    }

    public void setBarrel(String barrel) {
        this.barrel = barrel;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMagazine() {
        return magazine;
    }

    public void setMagazine(String magazine) {
        this.magazine = magazine;
    }

    public String getGrip() {
        return grip;
    }

    public void setGrip(String grip) {
        this.grip = grip;
    }

    /**
     * Template Method: standardizes the firing sequence for all weapons.
     * Angle is in radians, consistent with client/server projectile math.
     */
    public final void fireWeapon(ServerWorld world, Player owner, float angleRad) {
        if (isReloading(world.getGameTime())) {
            System.out.println(getName() + " is reloading, cannot fire.");
            return;
        }
        if (!hasAmmo()) {
            System.out.println(getName() + " attempted to fire with no ammo.");
            return;
        }
        consumeAmmo();

        int projectileCount = getProjectileCount();
        float spreadRad = getSpreadAngleInRadians();

        for (int i = 0; i < projectileCount; i++) {
            float finalAngle = calculateProjectileAngle(angleRad, spreadRad, i, projectileCount);
            createProjectile(world, owner, finalAngle);
        }

        applyEffects();
    }

    protected void consumeAmmo() {
        if (currentAmmo > 0) {
            currentAmmo -= 1;
            System.out.println(getName() + " consumed ammo. Remaining: " + currentAmmo + "/" + ammoCapacity);
        }
    }

    protected boolean hasAmmo() {
        return currentAmmo > 0;
    }

    private float calculateProjectileAngle(float baseAngleRad, float spreadRad, int projectileIndex,
                                           int totalProjectiles) {
        if (totalProjectiles <= 1 || spreadRad <= 0f)
            return baseAngleRad;
        float offset = -spreadRad / 2f + (projectileIndex * (spreadRad / (totalProjectiles - 1)));
        return baseAngleRad + offset;
    }

    // Abstract and hook methods for subclasses
    public abstract int getProjectileCount();

    public abstract float getSpreadAngleInRadians();

    public abstract void createProjectile(ServerWorld world, Player owner, float angleRad);

    protected void applyEffects() {
    }

    @Deprecated
    public abstract void shoot();

    public abstract String getDescription();
}