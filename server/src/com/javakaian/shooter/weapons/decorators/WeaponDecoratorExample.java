package com.javakaian.shooter.weapons.decorators;

import com.javakaian.shooter.weapons.Rifle;
import com.javakaian.shooter.weapons.Weapon;

/**
 * Tiny example showing how to chain weapon decorators.
 * Not wired into the game loop; run this class' main to see the output.
 */
public class WeaponDecoratorExample {
    public static void main(String[] args) {
        // Base weapon
        Weapon rifle = new Rifle();

        // Add attachments dynamically (Decorator chaining)
        Weapon upgraded = new ScopeAttachment(rifle, "4x ACOG", 150f);
        upgraded = new ExtendedMagazineAttachment(upgraded, 15);
        upgraded = new GripAttachment(upgraded, "Tactical Grip", 0.5f);
        upgraded = new DamageBoostAttachment(upgraded, 5f);

        // Use the decorated weapon
        System.out.println("Name: " + upgraded.getName());
        System.out.println("Damage: " + upgraded.getDamage());
        System.out.println("Range: " + upgraded.getRange());
        System.out.println("Fire Rate: " + upgraded.getFireRate());
        System.out.println("Ammo: " + upgraded.getAmmoCapacity());
        System.out.println("Scope: " + upgraded.getScope());
        System.out.println("Magazine: " + upgraded.getMagazine());
        System.out.println("Grip: " + upgraded.getGrip());
        System.out.println("Description: " + upgraded.getDescription());

        upgraded.shoot();
    }
}
