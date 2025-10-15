package com.javakaian.shooter.builder;

import com.javakaian.shooter.weapons.Rifle;
import com.javakaian.shooter.weapons.Shotgun;
import com.javakaian.shooter.weapons.Sniper;
import com.javakaian.shooter.weapons.Weapon;

public class WeaponFactory {
    
    public static Weapon createWeapon(String weaponType) {
        return switch (weaponType.toLowerCase()) {
            case "rifle" -> new Rifle();
            case "shotgun" -> new Shotgun();
            case "sniper" -> new Sniper();
            default -> throw new IllegalArgumentException("Unknown weapon type: " + weaponType);
        };
    }
}