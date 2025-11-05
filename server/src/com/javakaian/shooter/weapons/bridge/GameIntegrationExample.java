package com.javakaian.shooter.weapons.bridge;

import com.javakaian.shooter.shapes.Player;

/**
 * Practical integration example showing how to use the Bridge Pattern
 * with the existing Player class in the game.
 * 
 * This class demonstrates how to integrate the new Bridge-pattern weapons
 * with your existing game architecture.
 */
public class GameIntegrationExample {
    
    /**
     * Example 1: Adding Bridge weapons to Player without modifying Player class
     */
    public static class PlayerWeaponController {
        private Player player;
        private BridgeWeapon bridgeWeapon;
        
        public PlayerWeaponController(Player player) {
            this.player = player;
        }
        
        public void equipBridgeWeapon(BridgeWeapon weapon) {
            this.bridgeWeapon = weapon;
            System.out.println("Player " + player.getId() + " equipped: " + 
                             weapon.getFullDescription());
        }
        
        public void fire() {
            if (bridgeWeapon != null && bridgeWeapon.getCurrentAmmo() > 0) {
                bridgeWeapon.fire();
                player.recordShot(); // Use existing Player method
            } else if (bridgeWeapon != null) {
                System.out.println("Need to reload!");
            }
        }
        
        public void reload() {
            if (bridgeWeapon != null) {
                bridgeWeapon.reload();
            }
        }
        
        public void switchFiringMode(FiringMechanism mechanism) {
            if (bridgeWeapon != null) {
                bridgeWeapon.switchFiringMode(mechanism);
            }
        }
        
        public BridgeWeapon getCurrentBridgeWeapon() {
            return bridgeWeapon;
        }
    }
    
    /**
     * Example 2: Weapon loadout system
     */
    public static class WeaponLoadout {
        private BridgeWeapon primaryWeapon;
        private BridgeWeapon secondaryWeapon;
        private BridgeWeapon currentWeapon;
        
        public WeaponLoadout() {
            // Default loadout
            primaryWeapon = new AssaultRifle(new SingleShotMechanism());
            secondaryWeapon = new TacticalShotgun(new BurstFireMechanism());
            currentWeapon = primaryWeapon;
        }
        
        public void switchToPrimary() {
            currentWeapon = primaryWeapon;
            System.out.println("Switched to primary: " + currentWeapon.getFullDescription());
        }
        
        public void switchToSecondary() {
            currentWeapon = secondaryWeapon;
            System.out.println("Switched to secondary: " + currentWeapon.getFullDescription());
        }
        
        public void setPrimaryWeapon(BridgeWeapon weapon) {
            this.primaryWeapon = weapon;
        }
        
        public void setSecondaryWeapon(BridgeWeapon weapon) {
            this.secondaryWeapon = weapon;
        }
        
        public BridgeWeapon getCurrentWeapon() {
            return currentWeapon;
        }
        
        public void fire() {
            if (currentWeapon != null) {
                currentWeapon.fire();
            }
        }
        
        public void reload() {
            if (currentWeapon != null) {
                currentWeapon.reload();
            }
        }
        
        public void cycleFiringMode() {
            if (currentWeapon == null) return;
            
            FiringMechanism current = currentWeapon.getFiringMechanism();
            
            // Cycle through firing modes
            if (current instanceof SingleShotMechanism) {
                currentWeapon.switchFiringMode(new BurstFireMechanism());
            } else if (current instanceof BurstFireMechanism) {
                currentWeapon.switchFiringMode(new FullAutoMechanism());
            } else if (current instanceof FullAutoMechanism) {
                currentWeapon.switchFiringMode(new ChargedShotMechanism());
            } else {
                currentWeapon.switchFiringMode(new SingleShotMechanism());
            }
        }
    }
    
    /**
     * Example 3: Complete game scenario
     */
    public static void demonstrateGameIntegration() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         BRIDGE PATTERN - GAME INTEGRATION DEMO             ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Create a player (using existing Player class)
        Player player = new Player(100, 100, 32, 1);
        
        // Wrap with weapon controller
        PlayerWeaponController weaponController = new PlayerWeaponController(player);
        
        // Create a weapon loadout
        WeaponLoadout loadout = new WeaponLoadout();
        
        System.out.println("=== SCENARIO: Mission Start ===\n");
        
        // Starting loadout
        System.out.println("Player spawns with default loadout:");
        loadout.switchToPrimary();
        System.out.println();
        
        // Engage enemies at medium range
        System.out.println("--- Engaging enemies at medium range ---");
        weaponController.equipBridgeWeapon(loadout.getCurrentWeapon());
        weaponController.fire();
        weaponController.fire();
        System.out.println();
        
        // Switch firing mode for better control
        System.out.println("--- Too much recoil, switching to burst fire ---");
        loadout.cycleFiringMode();
        weaponController.equipBridgeWeapon(loadout.getCurrentWeapon());
        weaponController.fire();
        System.out.println();
        
        // Close quarters combat
        System.out.println("--- Enemies rushing! Switching to secondary weapon ---");
        loadout.switchToSecondary();
        weaponController.equipBridgeWeapon(loadout.getCurrentWeapon());
        weaponController.fire();
        weaponController.fire();
        System.out.println();
        
        // Running low on ammo
        System.out.println("--- Running low on ammo, reloading ---");
        weaponController.reload();
        System.out.println();
        
        // Boss fight - customize loadout
        System.out.println("--- Boss fight! Equipping specialized weapons ---");
        BridgeWeapon sniperRifle = new PrecisionSniper(new ChargedShotMechanism());
        loadout.setPrimaryWeapon(sniperRifle);
        loadout.switchToPrimary();
        weaponController.equipBridgeWeapon(loadout.getCurrentWeapon());
        
        System.out.println("Charging shot for maximum damage...");
        weaponController.fire();
        weaponController.fire();
        weaponController.bridgeWeapon.stopFiring();
        System.out.println();
        
        // Weapon customization during gameplay
        System.out.println("--- Mid-mission weapon customization ---");
        BridgeWeapon customRifle = new AssaultRifle(new FullAutoMechanism());
        customRifle.setDamage(20.0f); // Damage upgrade
        customRifle.setAmmoCapacity(45); // Extended mag
        loadout.setSecondaryWeapon(customRifle);
        loadout.switchToSecondary();
        weaponController.equipBridgeWeapon(loadout.getCurrentWeapon());
        weaponController.fire();
        weaponController.fire();
        weaponController.fire();
        weaponController.bridgeWeapon.stopFiring();
        System.out.println();
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║            INTEGRATION DEMO COMPLETE                       ║");
        System.out.println("║                                                            ║");
        System.out.println("║  The Bridge Pattern allows seamless integration with       ║");
        System.out.println("║  existing game systems while maintaining flexibility       ║");
        System.out.println("║  and extensibility.                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Example 4: Network integration for multiplayer
     */
    public static class NetworkWeaponSync {
        
        /**
         * Serialize weapon state for network transmission
         */
        public static String serializeWeapon(BridgeWeapon weapon) {
            if (weapon == null) return "";
            
            String weaponType = weapon.getClass().getSimpleName();
            String firingMech = weapon.getFiringMechanism().getClass().getSimpleName();
            int ammo = weapon.getCurrentAmmo();
            
            return String.format("%s:%s:%d", weaponType, firingMech, ammo);
        }
        
        /**
         * Deserialize weapon state from network data
         */
        public static BridgeWeapon deserializeWeapon(String data) {
            if (data == null || data.isEmpty()) return null;
            
            String[] parts = data.split(":");
            if (parts.length != 3) return null;
            
            String weaponType = parts[0];
            String firingMechType = parts[1];
            int ammo = Integer.parseInt(parts[2]);
            
            // Create firing mechanism
            FiringMechanism mechanism = createFiringMechanism(firingMechType);
            
            // Create weapon
            BridgeWeapon weapon = createWeapon(weaponType, mechanism);
            
            // Restore ammo state
            if (weapon != null) {
                int ammoToRemove = weapon.getCurrentAmmo() - ammo;
                for (int i = 0; i < ammoToRemove; i++) {
                    // Simulate shots to reduce ammo
                }
            }
            
            return weapon;
        }
        
        private static FiringMechanism createFiringMechanism(String type) {
            switch (type) {
                case "SingleShotMechanism": return new SingleShotMechanism();
                case "BurstFireMechanism": return new BurstFireMechanism();
                case "FullAutoMechanism": return new FullAutoMechanism();
                case "ChargedShotMechanism": return new ChargedShotMechanism();
                default: return new SingleShotMechanism();
            }
        }
        
        private static BridgeWeapon createWeapon(String type, FiringMechanism mechanism) {
            switch (type) {
                case "AssaultRifle": return new AssaultRifle(mechanism);
                case "TacticalShotgun": return new TacticalShotgun(mechanism);
                case "PrecisionSniper": return new PrecisionSniper(mechanism);
                case "SubmachineGun": return new SubmachineGun(mechanism);
                default: return new AssaultRifle(mechanism);
            }
        }
    }
    
    /**
     * Main method to run the integration demo
     */
    public static void main(String[] args) {
        demonstrateGameIntegration();
        
        System.out.println("\n\n=== NETWORK SERIALIZATION TEST ===\n");
        
        BridgeWeapon weapon = new AssaultRifle(new BurstFireMechanism());
        weapon.fire(); // Use some ammo
        
        String serialized = NetworkWeaponSync.serializeWeapon(weapon);
        System.out.println("Serialized: " + serialized);
        
        BridgeWeapon deserialized = NetworkWeaponSync.deserializeWeapon(serialized);
        System.out.println("Deserialized: " + deserialized.getFullDescription());
    }
}
