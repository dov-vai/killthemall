package com.javakaian.shooter.weapons.bridge;

/**
 * Decorator for BridgeWeapon - Silencer attachment
 * Reduces damage, increases fire rate, reduces bullet size
 */
public class SilencerDecorator extends BridgeWeapon {
    
    private final BridgeWeapon baseWeapon;
    private final String barrelName;
    private final float damagePenalty;
    
    public SilencerDecorator(BridgeWeapon baseWeapon, String barrelName, float damagePenalty) {
        super(baseWeapon.getName(), baseWeapon.getFiringMechanism());
        this.baseWeapon = baseWeapon;
        this.barrelName = barrelName;
        this.damagePenalty = Math.abs(damagePenalty);
        
        // Copy base stats with modifications
        this.damage = Math.max(1.0f, baseWeapon.getDamage() - damagePenalty); // LESS damage
        this.range = baseWeapon.getRange();
        this.baseFireRate = baseWeapon.getBaseFireRate() + 0.3f; // FASTER
        this.ammoCapacity = baseWeapon.getAmmoCapacity();
        this.currentAmmo = baseWeapon.getCurrentAmmo();
    }
    
    @Override
    public String getWeaponType() {
        return baseWeapon.getWeaponType() + " [Silencer]";
    }
    
    // Bullet size modifier (80% size for smaller bullets)
    public float getBulletSizeMultiplier() {
        return 0.8f;
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
