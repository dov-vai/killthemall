package com.javakaian.shooter.weapons.bridge;

/**
 * Bridge Pattern - Client/Demo Class
 * 
 * This class demonstrates the Bridge Design Pattern in action within 
 * the context of a 2D shooter game. It shows how weapons (abstraction) 
 * can be combined with different firing mechanisms (implementation) 
 * independently and dynamically at runtime.
 * 
 * KEY CONCEPTS DEMONSTRATED:
 * 1. Separation of abstraction (weapon types) from implementation (firing mechanisms)
 * 2. Composition over inheritance (weapons contain firing mechanisms)
 * 3. Runtime switching of implementations
 * 4. Polymorphic behavior across both hierarchies
 */
public class BridgePatternDemo {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     BRIDGE PATTERN DEMO - 2D SHOOTER WEAPON SYSTEM           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        
        // ============================================================
        // DEMO 1: Creating weapons with different firing mechanisms
        // ============================================================
        System.out.println("═══ DEMO 1: Weapon Creation with Different Firing Mechanisms ═══\n");
        
        // Create different firing mechanisms (Implementation hierarchy)
        FiringMechanism singleShot = new SingleShotMechanism();
        FiringMechanism burstFire = new BurstFireMechanism();
        FiringMechanism fullAuto = new FullAutoMechanism();
        FiringMechanism chargedShot = new ChargedShotMechanism();
        
        // Create weapons with different mechanisms (Abstraction hierarchy)
        BridgeWeapon rifle = new AssaultRifle(singleShot);
        BridgeWeapon shotgun = new TacticalShotgun(burstFire);
        BridgeWeapon sniper = new PrecisionSniper(chargedShot);
        BridgeWeapon smg = new SubmachineGun(fullAuto);
        
        System.out.println("Created 4 weapons with 4 different firing mechanisms:");
        System.out.println("1. " + rifle.getFullDescription());
        System.out.println("2. " + shotgun.getFullDescription());
        System.out.println("3. " + sniper.getFullDescription());
        System.out.println("4. " + smg.getFullDescription());
        System.out.println();
        
        // ============================================================
        // DEMO 2: Using weapons with their initial firing mechanisms
        // ============================================================
        System.out.println("\n═══ DEMO 2: Firing Weapons ═══\n");
        
        System.out.println("--- Assault Rifle (Single Shot) ---");
        rifle.fire();
        rifle.fire();
        System.out.println();
        
        System.out.println("--- Tactical Shotgun (Burst Fire) ---");
        shotgun.fire();
        System.out.println();
        
        System.out.println("--- Submachine Gun (Full Auto) ---");
        smg.fire();
        smg.fire();
        smg.fire();
        smg.stopFiring();
        System.out.println();
        
        System.out.println("--- Precision Sniper (Charged Shot) ---");
        sniper.fire(); // Start charging
        sniper.fire(); // Continue charging
        sniper.fire(); // Continue charging
        sniper.stopFiring(); // Release charged shot
        System.out.println();
        
        // ============================================================
        // DEMO 3: Runtime switching of firing mechanisms (Bridge Pattern key feature)
        // ============================================================
        System.out.println("\n═══ DEMO 3: Dynamic Firing Mechanism Switching ═══\n");
        
        System.out.println("The Bridge Pattern allows us to change the firing mechanism");
        System.out.println("of any weapon at runtime without changing the weapon itself!\n");
        
        // Switch assault rifle to burst fire
        System.out.println("--- Switching Assault Rifle from Single Shot to Burst Fire ---");
        rifle.switchFiringMode(burstFire);
        System.out.println("New configuration: " + rifle.getFullDescription());
        rifle.fire();
        System.out.println();
        
        // Switch shotgun to full auto (unusual but possible!)
        System.out.println("--- Switching Shotgun to Full Auto (AA-12 style!) ---");
        shotgun.switchFiringMode(fullAuto);
        System.out.println("New configuration: " + shotgun.getFullDescription());
        shotgun.fire();
        shotgun.fire();
        shotgun.stopFiring();
        System.out.println();
        
        // Switch sniper to single shot
        System.out.println("--- Switching Sniper to Single Shot for quick follow-ups ---");
        sniper.switchFiringMode(singleShot);
        System.out.println("New configuration: " + sniper.getFullDescription());
        sniper.fire();
        System.out.println();
        
        // Switch SMG to burst fire for better accuracy
        System.out.println("--- Switching SMG to Burst Fire for better control ---");
        smg.switchFiringMode(burstFire);
        System.out.println("New configuration: " + smg.getFullDescription());
        smg.fire();
        smg.fire();
        System.out.println();
        
        // ============================================================
        // DEMO 4: Reload mechanics
        // ============================================================
        System.out.println("\n═══ DEMO 4: Ammo Management and Reloading ═══\n");
        
        System.out.println("--- Emptying SMG magazine ---");
        for (int i = 0; i < 15; i++) {
            smg.fire();
        }
        
        System.out.println("\n--- Reloading SMG ---");
        smg.reload();
        System.out.println();
        
        // ============================================================
        // DEMO 5: Game scenario - Player switching weapons mid-combat
        // ============================================================
        System.out.println("\n═══ DEMO 5: In-Game Combat Scenario ═══\n");
        
        GamePlayer player = new GamePlayer("Player1");
        
        System.out.println("--- Long range engagement: Use Sniper ---");
        player.equipWeapon(sniper);
        player.shoot();
        player.stopShooting();
        System.out.println();
        
        System.out.println("--- Enemy closing in: Switch to Assault Rifle with Full Auto ---");
        rifle.switchFiringMode(fullAuto);
        player.equipWeapon(rifle);
        player.shoot();
        player.shoot();
        player.stopShooting();
        System.out.println();
        
        System.out.println("--- Close quarters: Switch to Shotgun with Single Shot ---");
        shotgun.switchFiringMode(singleShot);
        player.equipWeapon(shotgun);
        player.shoot();
        player.reload();
        System.out.println();
        
        // ============================================================
        // DEMO 6: Showing the flexibility - Same weapon, all mechanisms
        // ============================================================
        System.out.println("\n═══ DEMO 6: Ultimate Flexibility - One Weapon, All Mechanisms ═══\n");
        
        BridgeWeapon versatileRifle = new AssaultRifle(singleShot);
        
        System.out.println("Testing Assault Rifle with ALL firing mechanisms:\n");
        
        System.out.println("1. Single Shot Mode:");
        versatileRifle.fire();
        
        System.out.println("\n2. Burst Fire Mode:");
        versatileRifle.switchFiringMode(new BurstFireMechanism());
        versatileRifle.fire();
        
        System.out.println("\n3. Full Auto Mode:");
        versatileRifle.switchFiringMode(new FullAutoMechanism());
        versatileRifle.fire();
        versatileRifle.fire();
        versatileRifle.stopFiring();
        
        System.out.println("\n4. Charged Shot Mode:");
        versatileRifle.switchFiringMode(new ChargedShotMechanism());
        versatileRifle.fire();
        versatileRifle.fire();
        versatileRifle.stopFiring();
        
        // ============================================================
        // Summary
        // ============================================================
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   BRIDGE PATTERN SUMMARY                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║ Abstraction Hierarchy: Weapon Types                           ║");
        System.out.println("║   - BridgeWeapon (abstract)                                   ║");
        System.out.println("║   - AssaultRifle, TacticalShotgun, PrecisionSniper, SMG      ║");
        System.out.println("║                                                               ║");
        System.out.println("║ Implementation Hierarchy: Firing Mechanisms                   ║");
        System.out.println("║   - FiringMechanism (interface)                               ║");
        System.out.println("║   - SingleShot, BurstFire, FullAuto, ChargedShot             ║");
        System.out.println("║                                                               ║");
        System.out.println("║ Bridge: Composition via 'firingMechanism' field              ║");
        System.out.println("║                                                               ║");
        System.out.println("║ Benefits Demonstrated:                                        ║");
        System.out.println("║ ✓ Weapons and firing mechanisms vary independently            ║");
        System.out.println("║ ✓ Runtime mechanism switching without weapon changes          ║");
        System.out.println("║ ✓ Any weapon can use any firing mechanism                     ║");
        System.out.println("║ ✓ Easy to add new weapons or mechanisms without modifying    ║");
        System.out.println("║   existing code (Open/Closed Principle)                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}

/**
 * Simple GamePlayer class to demonstrate weapon usage in game context.
 */
class GamePlayer {
    private String name;
    private BridgeWeapon currentWeapon;
    
    public GamePlayer(String name) {
        this.name = name;
    }
    
    public void equipWeapon(BridgeWeapon weapon) {
        this.currentWeapon = weapon;
        System.out.println(name + " equipped: " + weapon.getFullDescription());
    }
    
    public void shoot() {
        if (currentWeapon != null) {
            System.out.println(name + " shooting:");
            currentWeapon.fire();
        } else {
            System.out.println(name + " has no weapon equipped!");
        }
    }
    
    public void stopShooting() {
        if (currentWeapon != null) {
            currentWeapon.stopFiring();
        }
    }
    
    public void reload() {
        if (currentWeapon != null) {
            System.out.println(name + " reloading:");
            currentWeapon.reload();
        }
    }
    
    public void switchFiringMode(FiringMechanism newMechanism) {
        if (currentWeapon != null) {
            currentWeapon.switchFiringMode(newMechanism);
        }
    }
}
