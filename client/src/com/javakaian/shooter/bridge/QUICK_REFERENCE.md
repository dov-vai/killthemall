# Bridge Pattern - Quick Reference

## 🎯 Pattern Summary

**Intent**: Decouple an abstraction from its implementation so both can vary independently.

**Game Context**: Separate weapon control modes (how to aim) from firing mechanisms (how to fire).

## 📦 Package Structure

```
client/src/com/javakaian/shooter/bridge/
├── FiringMechanism.java          (Implementor Interface)
├── SemiAutoFiring.java            (Concrete Implementor)
├── BurstFiring.java               (Concrete Implementor)
├── FullAutoFiring.java            (Concrete Implementor)
├── WeaponControl.java             (Abstraction)
├── ManualControl.java             (Refined Abstraction)
├── AutoAimControl.java            (Refined Abstraction)
├── BridgePatternClient.java       (Client/Demo)
└── README.md                      (Full Documentation)
```

## 🔗 The Bridge Connection

```java
public abstract class WeaponControl {
    protected FiringMechanism firingMechanism; // ← THE BRIDGE!

    public void setFiringMechanism(FiringMechanism fm) {
        this.firingMechanism = fm;
    }
}
```

## 🎮 Control Modes (Abstraction Side)

| Class              | Description           | Special Action                   |
| ------------------ | --------------------- | -------------------------------- |
| **ManualControl**  | Direct player control | Toggle precision mode (slow aim) |
| **AutoAimControl** | Auto-snap to enemies  | Lock target for 3 seconds        |

## 🔫 Firing Mechanisms (Implementation Side)

| Class              | Fire Rate    | Ammo | Behavior                           |
| ------------------ | ------------ | ---- | ---------------------------------- |
| **SemiAutoFiring** | 3/sec        | 15   | Single shot per trigger            |
| **BurstFiring**    | 2 bursts/sec | 30   | 3-round burst with spread          |
| **FullAutoFiring** | 10/sec       | 50   | Continuous fire, accuracy degrades |

## 🎪 Possible Combinations (6 total)

```
1. ManualControl + SemiAutoFiring
2. ManualControl + BurstFiring
3. ManualControl + FullAutoFiring
4. AutoAimControl + SemiAutoFiring
5. AutoAimControl + BurstFiring
6. AutoAimControl + FullAutoFiring
```

✨ **All combinations work seamlessly!**

## 💻 Code Example

```java
// Create any combination
WeaponControl control = new ManualControl(new SemiAutoFiring());

// Use it
Vector2 target = new Vector2(200, 200);
control.execute(target, player);

// Switch implementation at runtime (Bridge flexibility!)
control.setFiringMechanism(new BurstFiring());
control.execute(target, player);
```

## 🎯 Key Methods

### WeaponControl (Base Class)

```java
execute(target, player)      // Fire at target
aim(target, player)          // Calculate aim direction
reload()                     // Reload weapon
specialAction()              // Control-specific action
setFiringMechanism(fm)       // Change firing mechanism
update(deltaTime)            // Update state
```

### FiringMechanism (Interface)

```java
fire(position, direction, player)  // Fire bullets
reload()                            // Reload ammo
canFire()                           // Check if can fire
getFireRate()                       // Get fire rate
getAmmoCount()                      // Get current ammo
update(deltaTime)                   // Update cooldowns
```

## ✅ Benefits

1. **Independent Hierarchies**: Change controls OR firing without affecting the other
2. **Runtime Flexibility**: Switch firing mechanisms on-the-fly
3. **No Class Explosion**: 2 controls + 3 mechanisms = 5 classes (not 6!)
4. **Easy Extension**: Add new controls or mechanisms independently

## 🚀 Running the Demo

```bash
cd /Users/minion-2/killthemall
./gradlew compileJava
cd client/src
java com.javakaian.shooter.bridge.BridgePatternClient
```

## 📝 Adding New Components

### Add a New Control Mode

```java
public class GamepadControl extends WeaponControl {
    public GamepadControl(FiringMechanism firingMechanism) {
        super(firingMechanism);
        this.controlName = "Gamepad Control";
    }

    @Override
    public void execute(Vector2 targetPosition, Player player) {
        // Gamepad-specific execution logic
    }

    @Override
    public Vector2 aim(Vector2 targetPosition, Player player) {
        // Use analog stick for aiming
    }

    @Override
    public void specialAction() {
        // Vibration feedback
    }
}
```

### Add a New Firing Mechanism

```java
public class ChargedShotFiring implements FiringMechanism {
    private float chargeLevel = 0;

    @Override
    public void fire(Vector2 position, Vector2 direction, Player player) {
        // Fire based on charge level
    }

    // Implement other interface methods...
}
```

## 🎓 Learning Resources

- See `README.md` for full documentation
- See `BridgePatternClient.java` for working examples
- Each class has detailed JavaDoc comments

## 🔍 Pattern Recognition

**You know you need Bridge Pattern when:**

- You have two dimensions that vary independently
- You want to avoid a Cartesian product of classes (M × N combinations)
- You need runtime flexibility in implementation choice
- You want to hide implementation details from clients

**In this game:**

- Dimension 1: How to control/aim (Manual, AutoAim, ...)
- Dimension 2: How to fire (SemiAuto, Burst, FullAuto, ...)
- Result: Clean separation with full flexibility! 🎉
