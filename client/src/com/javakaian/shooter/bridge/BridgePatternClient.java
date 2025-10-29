package com.javakaian.shooter.bridge;

import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.shapes.Player;

/**
 * Client class demonstrating the Bridge Pattern implementation.
 * 
 * This class shows how different control modes (abstractions) can be combined
 * with different firing mechanisms (implementations) at runtime, demonstrating
 * the flexibility of the Bridge Pattern.
 * 
 * The Bridge Pattern allows us to:
 * 1. Change control modes independently of firing mechanisms
 * 2. Change firing mechanisms independently of control modes
 * 3. Add new control modes without modifying firing mechanisms
 * 4. Add new firing mechanisms without modifying control modes
 */
public class BridgePatternClient {
    
    private WeaponControl currentControl;
    private Player player;
    
    public BridgePatternClient() {
        // Create a player for demonstration
        this.player = new Player(100, 100, 20);
        player.setId(1);
        
        // Start with manual control and semi-auto firing
        this.currentControl = new ManualControl(new SemiAutoFiring());
    }
    
    /**
     * Demonstrates the Bridge Pattern by showing different combinations
     * of control modes and firing mechanisms.
     */
    public void demonstrateBridgePattern() {
        System.out.println("=".repeat(80));
        System.out.println("BRIDGE PATTERN DEMONSTRATION - 2D Shooter Game");
        System.out.println("=".repeat(80));
        
        // Demonstration 1: Manual Control with Semi-Auto Firing
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DEMO 1: Manual Control + Semi-Auto Firing");
        System.out.println("=".repeat(80));
        
        currentControl = new ManualControl(new SemiAutoFiring());
        demonstrateControl(new Vector2(200, 200));
        
        // Demonstration 2: Manual Control with Burst Firing
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DEMO 2: Manual Control + Burst Firing (showing Bridge flexibility)");
        System.out.println("=".repeat(80));
        
        // Bridge Pattern: Change implementation without changing abstraction
        currentControl.setFiringMechanism(new BurstFiring());
        demonstrateControl(new Vector2(250, 250));
        
        // Demonstration 3: Auto-Aim Control with Full-Auto Firing
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DEMO 3: Auto-Aim Control + Full-Auto Firing");
        System.out.println("=".repeat(80));
        
        currentControl = new AutoAimControl(new FullAutoFiring());
        demonstrateControl(new Vector2(300, 300));
        
        // Demonstration 4: Auto-Aim Control with Semi-Auto Firing
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DEMO 4: Auto-Aim Control + Semi-Auto Firing (runtime switching)");
        System.out.println("=".repeat(80));
        
        // Bridge Pattern: Change implementation at runtime
        currentControl.setFiringMechanism(new SemiAutoFiring());
        demonstrateControl(new Vector2(350, 350));
        
        // Demonstration 5: Special Actions
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DEMO 5: Special Actions for Different Control Modes");
        System.out.println("=".repeat(80));
        
        demonstrateSpecialActions();
        
        // Summary
        System.out.println("\n" + "=".repeat(80));
        System.out.println("BRIDGE PATTERN BENEFITS DEMONSTRATED:");
        System.out.println("=".repeat(80));
        System.out.println("✓ Separated control modes (how to aim) from firing mechanisms (how to fire)");
        System.out.println("✓ Changed firing mechanisms at runtime without affecting control logic");
        System.out.println("✓ Mixed and matched any control mode with any firing mechanism");
        System.out.println("✓ Each hierarchy can evolve independently");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Demonstrates a control mode with multiple operations.
     */
    private void demonstrateControl(Vector2 target) {
        System.out.println("\nPlayer position: " + player.getPosition());
        System.out.println("Target position: " + target);
        
        // Aim
        currentControl.aim(target, player);
        
        // Fire multiple times to show firing mechanism behavior
        for (int i = 0; i < 3; i++) {
            currentControl.execute(target, player);
            currentControl.update(0.2f); // Simulate time passing
        }
        
        // Reload
        currentControl.reload();
        
        // Fire once more after reload
        currentControl.execute(target, player);
    }
    
    /**
     * Demonstrates special actions for different control modes.
     */
    private void demonstrateSpecialActions() {
        // Manual control special action
        WeaponControl manualControl = new ManualControl(new SemiAutoFiring());
        System.out.println("\n--- Manual Control Special Action ---");
        manualControl.specialAction(); // Toggle precision mode
        manualControl.execute(new Vector2(200, 200), player);
        manualControl.specialAction(); // Toggle precision mode again
        manualControl.execute(new Vector2(200, 200), player);
        
        // Auto-aim control special action
        WeaponControl autoAimControl = new AutoAimControl(new FullAutoFiring());
        System.out.println("\n--- Auto-Aim Control Special Action ---");
        autoAimControl.specialAction(); // Lock target
        autoAimControl.execute(new Vector2(300, 300), player);
        autoAimControl.update(1.0f); // Simulate time passing
        autoAimControl.execute(new Vector2(400, 400), player); // Should still use locked target
        autoAimControl.specialAction(); // Release lock
    }
    
    /**
     * Switch to a different control mode.
     */
    public void switchControlMode(WeaponControl newControl) {
        this.currentControl = newControl;
        System.out.println("\nSwitched to control mode: " + newControl.getControlName());
    }
    
    /**
     * Get the current control mode.
     */
    public WeaponControl getCurrentControl() {
        return currentControl;
    }
    
    /**
     * Main method to run the demonstration.
     */
    public static void main(String[] args) {
        BridgePatternClient client = new BridgePatternClient();
        client.demonstrateBridgePattern();
    }
}
