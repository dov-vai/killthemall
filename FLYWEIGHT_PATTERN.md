# Flyweight Design Pattern Implementation

## Overview

This implementation demonstrates the **Flyweight Design Pattern** (GoF) in the KillThemAll 2D shooter game. The pattern is applied to `PowerUp` objects to optimize memory usage by sharing common rendering logic across multiple instances.

## Pattern Structure

```
┌─────────────────────┐
│ FlyweightFactory    │──────flyweights────────┐
│ +getFlyweight(key)  │                        │
└─────────────────────┘                        │
         │                                      ▼
         │                              ┌──────────────┐
         │                              │  Flyweight   │◁─┐
         │                              │ +operation() │  │
         │                              └──────────────┘  │
         │                                     ▲          │
         │                                     │          │
         │                      ┌──────────────┴──────────┴──────────┐
         │                      │                                     │
         │           ┌──────────────────────┐          ┌─────────────────────────────┐
         │           │ ConcreteFlyweight    │          │ UnsharedConcreteFlyweight   │
         │           │ -intrinsicState      │          │ -allStates                  │
         │           │ +operation()         │          │ +operation()                │
         │           └──────────────────────┘          └─────────────────────────────┘
         │                      ▲
         │                      │
         │           ┌──────────────────────┐
         └──────────▷│ Client (PowerUp)     │
                     └──────────────────────┘
```

## Components

### 1. **Flyweight Interface** (`Flyweight.java`)

Declares the `operation(ExtrinsicState)` method that accepts extrinsic state (position, size).

### 2. **ConcreteFlyweight** (`ConcreteFlyweight.java`)

- Stores **intrinsic state**: `PowerUpType` (SPEED_BOOST, DAMAGE_BOOST, SHIELD, AMMO_REFILL)
- Implements shared rendering logic based on the type
- **Shared** across all PowerUps of the same type

### 3. **UnsharedConcreteFlyweight** (`UnsharedConcreteFlyweight.java`)

- Stores **all states**: both intrinsic (type) and extrinsic (position, size)
- Useful when individual instances need full control over their state
- Demonstrates flexibility of the Flyweight pattern

### 4. **FlyweightFactory** (`FlyweightFactory.java`)

- Manages a pool of flyweight objects (one per `PowerUpType`)
- Implements the logic:
  ```
  if (flyweight[key] exists) {
      return existing flyweight;
  } else {
      create new flyweight;
      add to pool;
      return new flyweight;
  }
  ```
- Ensures flyweights are shared properly

### 5. **Client** (`PowerUp.java`)

- Stores **extrinsic state**: `position`, `size`
- Delegates rendering to the appropriate `Flyweight` obtained from `FlyweightFactory`
- Passes extrinsic state to the flyweight's `operation()` method

## How It Works

### Before Flyweight Pattern

```java
// Each PowerUp instance contains duplicate rendering logic
PowerUp speedBoost1 = new PowerUp(100, 100, 20, SPEED_BOOST);
PowerUp speedBoost2 = new PowerUp(200, 150, 20, SPEED_BOOST);
PowerUp speedBoost3 = new PowerUp(300, 200, 20, SPEED_BOOST);
// 3 instances × full rendering code = memory waste
```

### After Flyweight Pattern

```java
// All PowerUps of type SPEED_BOOST share one Flyweight object
PowerUp speedBoost1 = new PowerUp(100, 100, 20, SPEED_BOOST);
PowerUp speedBoost2 = new PowerUp(200, 150, 20, SPEED_BOOST);
PowerUp speedBoost3 = new PowerUp(300, 200, 20, SPEED_BOOST);

// Only 1 ConcreteFlyweight for SPEED_BOOST is created
// 3 instances share the same rendering logic
// Each stores only unique data: position (100,100), (200,150), (300,200)
```

## Memory Savings Example

- **7 PowerUp instances**:

  - 3 × SPEED_BOOST
  - 2 × DAMAGE_BOOST
  - 1 × SHIELD
  - 1 × AMMO_REFILL

- **Only 4 Flyweight objects** created (one per type)
- **Memory efficiency**: ~43% reduction in duplicate code

## Running the Demo

```bash
# Compile and run the demo
cd client/src
javac com/javakaian/shooter/shapes/flyweight/FlyweightDemo.java
java com.javakaian.shooter.shapes.flyweight.FlyweightDemo
```

**Expected Output**:

```
=== Flyweight Pattern Demonstration ===

Created 7 PowerUp instances:
- 3 SPEED_BOOST PowerUps
- 2 DAMAGE_BOOST PowerUps
- 1 SHIELD PowerUp
- 1 AMMO_REFILL PowerUp

Number of unique Flyweight objects created: 4

Expected: 4 (one for each PowerUpType)
This demonstrates memory efficiency!

Flyweight objects for SPEED_BOOST are the same instance: true
...
```

## Key Benefits

1. **Memory Efficiency**: Reduces memory footprint by sharing common data
2. **Performance**: Fewer objects = less garbage collection overhead
3. **Scalability**: Handles many objects efficiently (important in games with many PowerUps)
4. **Maintainability**: Rendering logic centralized in Flyweight classes

## Intrinsic vs Extrinsic State

| State Type    | Stored In           | Examples                             |
| ------------- | ------------------- | ------------------------------------ |
| **Intrinsic** | `ConcreteFlyweight` | PowerUpType, rendering colors/shapes |
| **Extrinsic** | `Client` (PowerUp)  | Position (x, y), size                |

**Intrinsic State**: Context-independent, shared across objects
**Extrinsic State**: Context-dependent, unique to each object

## Integration with Game

The Flyweight pattern is seamlessly integrated into the existing game:

1. `OMessageParser.getPowerUpsFromGWM()` creates PowerUp instances
2. PowerUp constructor remains unchanged for backward compatibility
3. `render()` method now uses Flyweight internally
4. No changes required in game states (PlayState, etc.)

## Testing

The `FlyweightDemo` class provides:

- Demonstration of pattern usage
- Verification that flyweights are shared
- Memory savings calculation
- Examples of both shared and unshared flyweights

## References

- **Design Patterns: Elements of Reusable Object-Oriented Software** (Gang of Four)
- Pattern Category: Structural
- Intent: Use sharing to support large numbers of fine-grained objects efficiently
