# Bridge Design Pattern Implementation - 2D Shooter Game

## Overview

This implementation demonstrates the **Bridge Design Pattern** applied to a 2D shooter game context. The pattern separates the abstraction of weapon control modes from the implementation of firing mechanisms, allowing them to vary independently.

## Pattern Structure

### The Bridge Pattern Components

1. **Abstraction**: `WeaponControl` - Defines how players control weapons
2. **Refined Abstractions**: `ManualControl`, `AutoAimControl` - Specific control implementations
3. **Implementor**: `FiringMechanism` - Defines how weapons fire bullets
4. **Concrete Implementors**: `SemiAutoFiring`, `BurstFiring`, `FullAutoFiring` - Specific firing behaviors
5. **Client**: `BridgePatternClient` - Uses the pattern

## UML Class Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                        BRIDGE PATTERN STRUCTURE                      │
└─────────────────────────────────────────────────────────────────────┘

ABSTRACTION HIERARCHY                    IMPLEMENTATION HIERARCHY
┌──────────────────────┐                ┌──────────────────────┐
│   <<abstract>>       │                │   <<interface>>      │
│   WeaponControl      │◆───────────────│  FiringMechanism     │
├──────────────────────┤                ├──────────────────────┤
│ - firingMech:        │                │ + fire()             │
│   FiringMechanism    │                │ + reload()           │
├──────────────────────┤                │ + canFire()          │
│ + execute()          │                │ + getFireRate()      │
│ + aim()              │                │ + getAmmoCount()     │
│ + reload()           │                │ + update()           │
│ + specialAction()    │                └──────────────────────┘
│ + setFiringMech()    │                         △
│ + update()           │                         │
└──────────────────────┘                ┌────────┴────────┐
         △                              │                 │
         │                              │                 │
    ┌────┴────┐                  ┌─────┴──────┐   ┌─────┴──────┐   ┌──────┴───────┐
    │         │                  │            │   │            │   │              │
┌───┴────────┐ ┌───────────┐  ┌─┴──────────┐ ┌──┴──────────┐ ┌───┴────────────┐
│Manual      │ │AutoAim    │  │SemiAuto    │ │Burst        │ │FullAuto        │
│Control     │ │Control    │  │Firing      │ │Firing       │ │Firing          │
├────────────┤ ├───────────┤  ├────────────┤ ├─────────────┤ ├────────────────┤
│+execute()  │ │+execute() │  │+fire()     │ │+fire()      │ │+fire()         │
│+aim()      │ │+aim()     │  │+reload()   │ │+reload()    │ │+reload()       │
│+reload()   │ │+reload()  │  │+canFire()  │ │+canFire()   │ │+canFire()      │
│+special()  │ │+special() │  │+getRate()  │ │+getRate()   │ │+getRate()      │
│+update()   │ │+update()  │  │+getAmmo()  │ │+getAmmo()   │ │+getAmmo()      │
│            │ │           │  │+update()   │ │+update()    │ │+update()       │
│-precision  │ │-locked    │  │            │ │+setBurst()  │ │-accuracy       │
│ Mode       │ │ Target    │  │            │ │            │ │ Penalty        │
└────────────┘ └───────────┘  └────────────┘ └─────────────┘ └────────────────┘

CLIENT
┌──────────────────────┐
│ BridgePatternClient  │
├──────────────────────┤
│ - currentControl:    │
│   WeaponControl      │
│ - player: Player     │
├──────────────────────┤
│ + demonstrate()      │
│ + switchControl()    │
└──────────────────────┘
```

## How It Works

### The Bridge Connection

The `WeaponControl` abstract class holds a reference to a `FiringMechanism` interface. This is the "bridge" that connects the two hierarchies:

```java
public abstract class WeaponControl {
    protected FiringMechanism firingMechanism; // The bridge!

    public void setFiringMechanism(FiringMechanism firingMechanism) {
        this.firingMechanism = firingMechanism;
    }
}
```

### Abstraction Hierarchy: Control Modes

**1. WeaponControl (Base Abstraction)**

- Maintains reference to FiringMechanism
- Defines common operations: `execute()`, `aim()`, `reload()`, `specialAction()`
- Allows runtime switching of firing mechanisms

**2. ManualControl (Refined Abstraction)**

- Player has direct control over aiming
- Special action: Toggle precision mode (slower aim, higher accuracy)
- Works with any firing mechanism

**3. AutoAimControl (Refined Abstraction)**

- Automatically aims at nearest enemy
- Special action: Lock target for sustained fire
- Works with any firing mechanism

### Implementation Hierarchy: Firing Mechanisms

**1. FiringMechanism (Implementor Interface)**

- Defines firing operations: `fire()`, `reload()`, `canFire()`
- Manages ammo, cooldowns, and fire rate

**2. SemiAutoFiring (Concrete Implementor)**

- Fires one bullet per trigger pull
- Moderate fire rate (3 bullets/second)
- 15 rounds per magazine

**3. BurstFiring (Concrete Implementor)**

- Fires 3-round bursts
- Bullets spread slightly within burst
- 30 rounds per magazine

**4. FullAutoFiring (Concrete Implementor)**

- Continuous fire while trigger held
- High fire rate (10 bullets/second)
- Accuracy degrades with sustained fire
- 50 rounds per magazine

## Key Benefits Demonstrated

### 1. Independent Variation

Control modes and firing mechanisms can change independently:

```java
// Change firing mechanism without changing control mode
manualControl.setFiringMechanism(new BurstFiring());
```

### 2. Runtime Flexibility

Mix and match any combination at runtime:

```java
WeaponControl control1 = new ManualControl(new SemiAutoFiring());
WeaponControl control2 = new ManualControl(new BurstFiring());
WeaponControl control3 = new AutoAimControl(new FullAutoFiring());
```

### 3. Easy Extension

Add new control modes without touching firing mechanisms:

```java
// New control mode works with all existing firing mechanisms
public class TouchScreenControl extends WeaponControl { ... }
```

Add new firing mechanisms without touching control modes:

```java
// New firing mechanism works with all existing control modes
public class ChargedShotFiring implements FiringMechanism { ... }
```

### 4. Reduced Class Explosion

Without Bridge Pattern, you'd need a class for every combination:

- ManualSemiAuto, ManualBurst, ManualFullAuto
- AutoAimSemiAuto, AutoAimBurst, AutoAimFullAuto
- = 6 classes (and grows exponentially with each addition)

With Bridge Pattern, you need only:

- 2 control modes + 3 firing mechanisms = 5 classes
- Any combination is possible through composition

## Usage Example

```java
// Create player
Player player = new Player(100, 100, 20);

// Create control with manual aiming and semi-auto firing
WeaponControl control = new ManualControl(new SemiAutoFiring());

// Use the control
Vector2 target = new Vector2(200, 200);
control.execute(target, player);

// Switch to burst firing at runtime (Bridge Pattern flexibility!)
control.setFiringMechanism(new BurstFiring());
control.execute(target, player);

// Switch to completely different control mode
control = new AutoAimControl(new FullAutoFiring());
control.execute(target, player);

// Use special actions
control.specialAction(); // Lock target for auto-aim
```

## Running the Demonstration

To see the Bridge Pattern in action, run the client:

```bash
cd client/src
javac com/javakaian/shooter/bridge/*.java
java com.javakaian.shooter.bridge.BridgePatternClient
```

The demonstration shows:

1. Manual Control + Semi-Auto Firing
2. Manual Control + Burst Firing (runtime switch)
3. Auto-Aim Control + Full-Auto Firing
4. Auto-Aim Control + Semi-Auto Firing (runtime switch)
5. Special actions for each control mode

## Game Integration

To integrate this into the actual game (PlayState):

```java
public class PlayState extends State {
    private WeaponControl weaponControl;

    public PlayState(StateController sc) {
        super(sc);
        // Initialize with default control
        weaponControl = new ManualControl(new SemiAutoFiring());
    }

    public void handleInput() {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 target = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            weaponControl.execute(target, player);
        }

        if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
            weaponControl.setFiringMechanism(new SemiAutoFiring());
        }
        if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
            weaponControl.setFiringMechanism(new BurstFiring());
        }
        if (Gdx.input.isKeyJustPressed(Keys.NUM_3)) {
            weaponControl.setFiringMechanism(new FullAutoFiring());
        }
    }
}
```

## Class Responsibilities

### WeaponControl (Abstraction)

- Define interface for control operations
- Maintain reference to FiringMechanism (the bridge)
- Delegate firing to FiringMechanism
- Handle control-specific behavior (aiming, special actions)

### ManualControl / AutoAimControl (Refined Abstractions)

- Implement specific aiming logic
- Implement control-specific special actions
- Use FiringMechanism for actual firing

### FiringMechanism (Implementor)

- Define interface for firing operations
- Manage weapon state (ammo, cooldowns)

### SemiAutoFiring / BurstFiring / FullAutoFiring (Concrete Implementors)

- Implement specific firing behavior
- Manage firing-specific state
- Independent of control mode

### BridgePatternClient (Client)

- Create and configure control modes
- Create and configure firing mechanisms
- Combine them through the bridge
- Demonstrate runtime flexibility

## Design Principles Applied

1. **Single Responsibility**: Each class has one reason to change

   - Control classes handle aiming/control logic
   - Firing classes handle bullet generation logic

2. **Open/Closed**: Open for extension, closed for modification

   - Add new control modes without modifying firing mechanisms
   - Add new firing mechanisms without modifying control modes

3. **Dependency Inversion**: Depend on abstractions, not concretions

   - WeaponControl depends on FiringMechanism interface, not concrete classes

4. **Composition over Inheritance**: Uses composition (bridge) instead of complex inheritance
   - Avoids exponential class explosion
   - Enables runtime flexibility

## Comparison with Other Patterns

- **Strategy Pattern**: Bridge is similar but affects two hierarchies instead of one
- **Adapter Pattern**: Adapter makes interfaces compatible; Bridge separates abstraction from implementation
- **Abstract Factory**: Could be used to create control-mechanism pairs, but Bridge allows mixing

## Conclusion

This Bridge Pattern implementation demonstrates how to decouple weapon control modes from firing mechanisms in a 2D shooter game. The pattern provides flexibility, extensibility, and maintainability by allowing both hierarchies to evolve independently while being combined in any configuration at runtime.
