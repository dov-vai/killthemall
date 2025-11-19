package com.javakaian.shooter.weapons.bridge;

/**
 * Decorator for BridgeWeapon - Damage Boost attachment
 * Increases damage and bullet size
 */
public class DamageBoostDecorator extends BridgeWeapon {

    private final BridgeWeapon baseWeapon;
    private final float damageBonus;

    public DamageBoostDecorator(BridgeWeapon baseWeapon, float damageBonus) {
        super(baseWeapon.getName(), baseWeapon.getFiringMechanism());
        this.baseWeapon = baseWeapon;
        this.damageBonus = damageBonus;

        // Copy base stats with modifications
        this.damage = baseWeapon.getDamage() + damageBonus; // MORE damage
        this.range = baseWeapon.getRange();
        this.baseFireRate = baseWeapon.getBaseFireRate();
        this.ammoCapacity = baseWeapon.getAmmoCapacity();
        this.currentAmmo = baseWeapon.getCurrentAmmo();
    }

    @Override
    public String getWeaponType() {
        return baseWeapon.getWeaponType() + " [Damage+]";
    }

    // Bullet size modifier (120% size for bigger bullets)
    public float getBulletSizeMultiplier() {
        return 1.2f;
    }

    @Override
    public void decrementAmmo(int amount) {
        super.decrementAmmo(amount);
        baseWeapon.decrementAmmo(amount);
    }

    @Override
    public void reload() {
        super.reload();
        baseWeapon.reload();
    }
}
