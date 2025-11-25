package com.javakaian.shooter.shapes.flyweight;

import com.javakaian.shooter.shapes.PowerUp;
import com.javakaian.shooter.shapes.PowerUp.PowerUpType;

/**
 * FlyweightDemo demonstrates the Flyweight pattern implementation.
 * 
 * This class shows how:
 * 1. Multiple PowerUp instances of the same type share the same Flyweight object
 * 2. The FlyweightFactory manages and reuses Flyweight instances
 * 3. Extrinsic state (position, size) is stored in the Client (PowerUp)
 * 4. Intrinsic state (PowerUpType and rendering logic) is stored in ConcreteFlyweight
 * 
 * Example usage:
 * <pre>
 * FlyweightDemo demo = new FlyweightDemo();
 * demo.demonstrateFlyweightPattern();
 * </pre>
 */
public class FlyweightDemo {
    
    /**
     * Demonstrates the Flyweight pattern with PowerUps.
     * Creates multiple PowerUp instances and shows that they share
     * Flyweight objects when they have the same type.
     */
    public void demonstrateFlyweightPattern() {
        System.out.println("=== Flyweight Pattern Demonstration ===\n");
        
        // Create multiple PowerUps of different types
        PowerUp speedBoost1 = new PowerUp(100, 100, 20, PowerUpType.SPEED_BOOST);
        PowerUp speedBoost2 = new PowerUp(200, 150, 20, PowerUpType.SPEED_BOOST);
        PowerUp speedBoost3 = new PowerUp(300, 200, 20, PowerUpType.SPEED_BOOST);
        
        PowerUp damageBoost1 = new PowerUp(400, 100, 20, PowerUpType.DAMAGE_BOOST);
        PowerUp damageBoost2 = new PowerUp(500, 150, 20, PowerUpType.DAMAGE_BOOST);
        
        PowerUp shield = new PowerUp(600, 100, 20, PowerUpType.SHIELD);
        PowerUp ammoRefill = new PowerUp(700, 100, 20, PowerUpType.AMMO_REFILL);
        
        System.out.println("Created 7 PowerUp instances:");
        System.out.println("- 3 SPEED_BOOST PowerUps");
        System.out.println("- 2 DAMAGE_BOOST PowerUps");
        System.out.println("- 1 SHIELD PowerUp");
        System.out.println("- 1 AMMO_REFILL PowerUp");
        System.out.println();
        
        // Get the factory and show how many unique flyweights were created
        FlyweightFactory factory = PowerUp.getFactory();
        int flyweightCount = factory.getFlyweightCount();
        
        System.out.println("Number of unique Flyweight objects created: " + flyweightCount);
        System.out.println();
        System.out.println("Expected: 4 (one for each PowerUpType)");
        System.out.println("This demonstrates memory efficiency!");
        System.out.println();
        
        // Demonstrate that the same flyweight is reused
        Flyweight flyweight1 = factory.getFlyweight(PowerUpType.SPEED_BOOST);
        Flyweight flyweight2 = factory.getFlyweight(PowerUpType.SPEED_BOOST);
        
        System.out.println("Flyweight objects for SPEED_BOOST are the same instance: " + 
                          (flyweight1 == flyweight2));
        System.out.println();
        
        // Show memory savings calculation
        System.out.println("=== Memory Savings ===");
        System.out.println("Without Flyweight: Each PowerUp stores its own rendering logic");
        System.out.println("With Flyweight: Rendering logic is shared across PowerUps of the same type");
        System.out.println();
        System.out.println("In this example:");
        System.out.println("- 7 PowerUp instances (clients)");
        System.out.println("- Only 4 Flyweight objects (shared rendering logic)");
        System.out.println("- Memory saved: ~43% reduction in duplicate rendering code");
        
        System.out.println("\n=== End of Demonstration ===");
    }
    
    /**
     * Main method to run the demonstration.
     */
    public static void main(String[] args) {
        FlyweightDemo demo = new FlyweightDemo();
        demo.demonstrateFlyweightPattern();
    }
    
    /**
     * Example showing UnsharedConcreteFlyweight usage.
     * UnsharedConcreteFlyweight stores all state internally,
     * unlike the shared ConcreteFlyweight.
     */
    public void demonstrateUnsharedFlyweight() {
        System.out.println("\n=== UnsharedConcreteFlyweight Demonstration ===\n");
        
        // Create UnsharedConcreteFlyweight instances
        UnsharedConcreteFlyweight unshared1 = 
            new UnsharedConcreteFlyweight(PowerUpType.SPEED_BOOST, 100, 100, 20);
        UnsharedConcreteFlyweight unshared2 = 
            new UnsharedConcreteFlyweight(PowerUpType.SPEED_BOOST, 200, 150, 20);
        
        System.out.println("Created 2 UnsharedConcreteFlyweight instances");
        System.out.println("Even though they are the same type, they are separate objects");
        System.out.println("They store all state (including position and size) internally");
        System.out.println();
        System.out.println("Unshared1 position: (" + unshared1.getX() + ", " + unshared1.getY() + ")");
        System.out.println("Unshared2 position: (" + unshared2.getX() + ", " + unshared2.getY() + ")");
        System.out.println();
        System.out.println("This is useful when you need full control over individual instances");
        System.out.println("while still participating in the Flyweight pattern structure.");
        
        System.out.println("\n=== End of Demonstration ===");
    }
}
