package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Concrete Implementation 4
 * 
 * ChargedShotMechanism charges up before firing a powerful shot.
 * The longer the charge, the more damage dealt.
 */
public class ChargedShotMechanism implements FiringMechanism {
    
    private boolean isCharging = false;
    private float chargeLevel = 0.0f;
    private static final float MAX_CHARGE = 100.0f;
    
    @Override
    public int startFire(String weaponName, float damage) {
        if (!isCharging) {
            isCharging = true;
            chargeLevel = 0.0f;
            System.out.println("[CHARGED SHOT] " + weaponName + " is charging...");
            return 0; // No projectile yet, still charging
        } else {
            // Continue charging
            chargeLevel = Math.min(chargeLevel + 10.0f, MAX_CHARGE);
            return 0;
        }
    }
    
    @Override
    public void stopFire(String weaponName) {
        if (isCharging) {
            float chargeMultiplier = chargeLevel / MAX_CHARGE;
            isCharging = false;
            System.out.println("[CHARGED SHOT] " + weaponName + " released charged shot! " +
                             "Charge: " + (int)(chargeMultiplier * 100) + "% " +
                             "(Damage multiplier: " + String.format("%.2f", chargeMultiplier) + "x)");
            chargeLevel = 0.0f;
        }
    }
    
    @Override
    public String getDescription() {
        return "Charged Shot";
    }
    
    @Override
    public float getFireRateMultiplier() {
        return 0.5f; // Much slower due to charging time
    }
    
    public float getChargeLevel() {
        return chargeLevel;
    }
    
    public float getChargeMultiplier() {
        return chargeLevel / MAX_CHARGE;
    }
}
