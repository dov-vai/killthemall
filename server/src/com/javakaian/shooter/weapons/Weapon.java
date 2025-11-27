package com.javakaian.shooter.weapons;

import com.esotericsoftware.kryonet.Server;
import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.weapons.state.ReadyState;
import com.javakaian.shooter.weapons.state.WeaponListener;
import com.javakaian.shooter.weapons.state.WeaponState;

import java.util.ArrayList;
import java.util.List;

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
    protected WeaponState state;
    private List<WeaponListener> listeners;

    public Weapon(String name) {
        this.name = name;
        this.damage = 10.0f;
        this.range = 100.0f;
        this.fireRate = 1.0f;
        this.ammoCapacity = 30;
        this.currentAmmo = this.ammoCapacity;
        this.state = new ReadyState();
        this.listeners = new ArrayList<>();
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
        notifyListeners();
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
        consumeAmmo();

        int projectileCount = getProjectileCount();
        float spreadRad = getSpreadAngleInRadians();

        for (int i = 0; i < projectileCount; i++) {
            float finalAngle = calculateProjectileAngle(angleRad, spreadRad, i, projectileCount);
            createProjectile(world, owner, finalAngle);
        }

        applyEffects();
    }

    public void setState(WeaponState state) {
        this.state = state;
    }

    public void requestFire(ServerWorld world, Player owner, float angleRad) {
        state.attemptFire(this, world, owner, angleRad);
    }

    public void requestReload() {
        state.attemptReload(this);
    }

    public void update(float deltaTime) {
        state.update(this, deltaTime);
    }

    protected void consumeAmmo() {
        if (currentAmmo > 0) {
            currentAmmo -= 1;
            System.out.println(getName() + " consumed ammo. Remaining: " + currentAmmo + "/" + ammoCapacity);
            notifyListeners();
        }
    }

    private float calculateProjectileAngle(float baseAngleRad, float spreadRad, int projectileIndex,
                                           int totalProjectiles) {
        if (totalProjectiles <= 1 || spreadRad <= 0f)
            return baseAngleRad;
        float offset = -spreadRad / 2f + (projectileIndex * (spreadRad / (totalProjectiles - 1)));
        return baseAngleRad + offset;
    }

    public void addListener(WeaponListener listener) {
        this.listeners.add(listener);
    }

    public void notifyListeners() {
        for (var listener : listeners) {
            listener.onAmmoChanged(this);
        }
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