# Flyweight Design Pattern Implementation

## Overview

This implementation demonstrates the **Flyweight Design Pattern** (GoF) in the KillThemAll 2D shooter game. The pattern is applied to `PowerUp` objects to optimize memory usage by sharing common rendering logic across multiple instances.

## Pattern Structure

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         FLYWEIGHT PATTERN STRUCTURE                             │
│                    Integration with Existing Game Classes                       │
└─────────────────────────────────────────────────────────────────────────────────┘

                              EXISTING GAME CLASSES
    ┌──────────────────────────────────────────────────────────────────────────┐
    │                                                                          │
    │  ┌─────────────────────┐      ┌──────────────────┐                      │
    │  │ OMessageParser      │      │ GameWorldMessage │                      │
    │  │ (existing)          │◄─────│ (existing)       │                      │
    │  │                     │      │ - powerUps[]     │                      │
    │  │ +getPowerUpsFromGWM │      │ - enemies[]      │                      │
    │  └──────────┬──────────┘      │ - bullets[]      │                      │
    │             │                 └──────────────────┘                      │
    │             │ creates                                                    │
    │             ▼                                                            │
    │  ┌─────────────────────┐      ┌──────────────────┐                      │
    │  │ PlayState           │◄─────│ StateController  │                      │
    │  │ (existing)          │      │ (existing)       │                      │
    │  │                     │      └──────────────────┘                      │
    │  │ - powerUps: List    │                                                 │
    │  │ - player: Player    │──────┐                                         │
    │  │ - enemies: List     │      │                                         │
    │  │ - bullets: List     │      │                                         │
    │  │                     │      │                                         │
    │  │ +render()           │      │                                         │
    │  │ +update()           │      ▼                                         │
    │  └──────────┬──────────┘  ┌──────────────────┐                          │
    │             │             │ Player           │                          │
    │             │             │ (existing)       │                          │
    │             │             │ - position       │                          │
    │             │             │ - health         │                          │
    │             │             │ - id             │                          │
    │             │             └──────────────────┘                          │
    │             │                                                            │
    └─────────────┼────────────────────────────────────────────────────────────┘
                  │ renders
                  ▼
    ┌──────────────────────────────────────────────────────────────────────────┐
    │                          FLYWEIGHT PATTERN                               │
    │                                                                          │
    │  ┌─────────────────────┐                                                │
    │  │ FlyweightFactory    │──────flyweights────────┐                       │
    │  │ +getFlyweight(key)  │                        │                       │
    │  └─────────────────────┘                        │                       │
    │           │                                      ▼                       │
    │           │                              ┌──────────────┐               │
    │           │                              │  Flyweight   │◁─┐            │
    │           │                              │ +operation() │  │            │
    │           │                              └──────────────┘  │            │
    │           │                                     ▲          │            │
    │           │                                     │          │            │
    │           │                      ┌──────────────┴──────────┴───────┐    │
    │           │                      │                                 │    │
    │           │           ┌──────────────────────┐    ┌────────────────────┐│
    │           │           │ ConcreteFlyweight    │    │UnsharedConcrete    ││
    │           │           │ -intrinsicState      │    │Flyweight           ││
    │           │           │ +operation()         │    │ -allStates         ││
    │           │           └──────────────────────┘    └────────────────────┘│
    │           │                      ▲                                      │
    │           │                      │                                      │
    │           │           ┌──────────────────────┐                          │
    │           └──────────▷│ PowerUp (Client)     │                          │
    │                       │ (modified)           │                          │
    │                       │ - position: Vector2  │◄─────┐                   │
    │                       │ - size: float        │      │                   │
    │                       │ - type: PowerUpType  │      │                   │
    │                       │ + render()           │      │                   │
    │                       └──────────────────────┘      │                   │
    │                                                      │                   │
    └──────────────────────────────────────────────────────┼───────────────────┘
                                                           │
    ┌──────────────────────────────────────────────────────┼───────────────────┐
    │                    LIBGDX FRAMEWORK                  │                   │
    │                                                      │                   │
    │  ┌─────────────────────┐      ┌──────────────────┐  │                   │
    │  │ ShapeRenderer       │      │ Vector2          │──┘                   │
    │  │ (libgdx)            │      │ (libgdx)         │                      │
    │  │                     │      │ - x: float       │                      │
    │  │ +setColor()         │      │ - y: float       │                      │
    │  │ +circle()           │      └──────────────────┘                      │
    │  │ +rect()             │                                                 │
    │  └─────────────────────┘      ┌──────────────────┐                      │
    │           ▲                   │ Color            │                      │
    │           │                   │ (libgdx)         │                      │
    │           │ used by           │ GREEN, PINK,     │                      │
    │           │ operation()       │ BLUE, YELLOW     │                      │
    │           │                   └──────────────────┘                      │
    └───────────┼──────────────────────────────────────────────────────────────┘
                │
                │
    ┌───────────┴──────────────────────────────────────────────────────────────┐
    │                    OTHER GAME SHAPES (Similar Pattern)                   │
    │                                                                          │
    │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐          │
    │  │ Enemy           │  │ Bullet          │  │ Spike           │          │
    │  │ (existing)      │  │ (existing)      │  │ (existing)      │          │
    │  │ - position      │  │ - position      │  │ - position      │          │
    │  │ - size          │  │ - size          │  │ - size          │          │
    │  │ - color         │  │ - visible       │  │ + render()      │          │
    │  │ + render()      │  │ + render()      │  │                 │          │
    │  └─────────────────┘  └─────────────────┘  └─────────────────┘          │
    │                                                                          │
    │  (Could also benefit from Flyweight pattern in future)                  │
    └──────────────────────────────────────────────────────────────────────────┘
```

## Class Relationships Table

| Existing Class     | Role in Pattern    | Connection                                           |
| ------------------ | ------------------ | ---------------------------------------------------- |
| `PlayState`        | Holds PowerUp list | Renders PowerUps via `GameManagerFacade`             |
| `OMessageParser`   | Creates PowerUps   | `getPowerUpsFromGWM()` creates PowerUp instances     |
| `GameWorldMessage` | Network data       | Contains PowerUp positions from server               |
| `ShapeRenderer`    | Rendering          | Used by Flyweight `operation()` to draw              |
| `Vector2`          | Position storage   | PowerUp stores position as extrinsic state           |
| `Color`            | Rendering colors   | ConcreteFlyweight uses for PowerUp types             |
| `StateController`  | State management   | Controls PlayState lifecycle                         |
| `Player`           | Game entity        | Collects PowerUps and stores in inventory            |
| `PowerUpInventory` | Storage system     | Stores collected PowerUps for later use (F1-F4)      |
| `Enemy`            | Game entity        | Similar shape pattern, potential Flyweight candidate |
| `Bullet`           | Game entity        | Similar shape pattern, potential Flyweight candidate |

## Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           DATA FLOW                                         │
└─────────────────────────────────────────────────────────────────────────────┘

  Server                    Network                      Client
┌─────────┐              ┌───────────┐              ┌─────────────────────────┐
│ Server  │   UDP/TCP    │ GameWorld │              │                         │
│ World   │─────────────▶│ Message   │─────────────▶│  OMessageParser         │
│         │              │           │              │  .getPowerUpsFromGWM()  │
└─────────┘              └───────────┘              └───────────┬─────────────┘
                                                                │
                                                                │ creates
                                                                ▼
                                                    ┌─────────────────────────┐
                                                    │  List<PowerUp>          │
                                                    │  ┌─────────┐            │
                                                    │  │PowerUp 1│──┐         │
                                                    │  └─────────┘  │         │
                                                    │  ┌─────────┐  │ share   │
                                                    │  │PowerUp 2│──┤         │
                                                    │  └─────────┘  │         │
                                                    │  ┌─────────┐  │         │
                                                    │  │PowerUp 3│──┘         │
                                                    │  └─────────┘            │
                                                    └───────────┬─────────────┘
                                                                │
                                                                ▼
                                                    ┌─────────────────────────┐
                                                    │  FlyweightFactory       │
                                                    │  ┌───────────────────┐  │
                                                    │  │ SPEED_BOOST → Fw1 │  │
                                                    │  │ DAMAGE_BOOST→ Fw2 │  │
                                                    │  │ SHIELD     → Fw3 │  │
                                                    │  │ AMMO_REFILL→ Fw4 │  │
                                                    │  └───────────────────┘  │
                                                    └───────────┬─────────────┘
                                                                │
                                                                │ render
                                                                ▼
                                                    ┌─────────────────────────┐
                                                    │  ShapeRenderer          │
                                                    │  (draws to screen)      │
                                                    └─────────────────────────┘
```

## Rendering Flow with Existing Classes

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                 RENDER FLOW: PowerUp.render() with Flyweight                │
└─────────────────────────────────────────────────────────────────────────────┘

  PlayState                Pattern Classes              LibGDX Framework
  ─────────                ───────────────              ────────────────

  ┌─────────────────────┐
  │ PlayState.render()  │
  │ (existing)          │
  │                     │
  │ for each powerUp:   │
  │   powerUp.render(sr)│
  └──────────┬──────────┘
             │
             │ calls
             ▼
  ┌─────────────────────┐
  │ PowerUp.render(sr)  │
  │ (modified)          │
  │                     │
  │ 1. Get flyweight    │───────────────────┐
  │ 2. Call operation() │                   │
  └──────────┬──────────┘                   │
             │                              │
             ▼                              ▼
  ┌─────────────────────┐      ┌─────────────────────┐
  │ FlyweightFactory    │      │ Flyweight fw =      │
  │ .getFlyweight(type) │─────▶│ factory.getFlyweight│
  │                     │      │ (PowerUpType.       │
  │ Check if exists:    │      │  SPEED_BOOST)       │
  │ - YES → return      │      └──────────┬──────────┘
  │ - NO  → create      │                 │
  └─────────────────────┘                 │
                                          │
             ┌────────────────────────────┘
             │
             ▼
  ┌─────────────────────┐
  │ ConcreteFlyweight   │
  │ .operation(sr,      │
  │            x, y,    │◄────── Extrinsic State
  │            size)    │        (from PowerUp)
  │                     │
  │ Intrinsic State:    │
  │ - type: SPEED_BOOST │
  │ - color: GREEN      │
  └──────────┬──────────┘
             │
             │ uses
             ▼
                                          ┌──────────────────────┐
                                          │ ShapeRenderer        │
                                          │ (libgdx - existing)  │
                                          │                      │
                                          │ sr.setColor(GREEN)   │
                                          │ sr.circle(x, y, r)   │
                                          │ sr.rect(x, y, w, h)  │
                                          └──────────────────────┘
                                                    │
                                                    ▼
                                          ┌──────────────────────┐
                                          │ Color                │
                                          │ (libgdx - existing)  │
                                          │                      │
                                          │ Color.GREEN          │
                                          │ Color.PINK           │
                                          │ Color.BLUE           │
                                          │ Color.YELLOW         │
                                          └──────────────────────┘
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              MEMORY COMPARISON: Before vs After Flyweight                   │
└─────────────────────────────────────────────────────────────────────────────┘

  BEFORE (without Flyweight)              AFTER (with Flyweight)

  ┌──────────────────────┐                ┌──────────────────────┐
  │ PowerUp 1            │                │ PowerUp 1            │
  │ - position (100,100) │                │ - position (100,100) │
  │ - size: 20           │                │ - size: 20           │
  │ - type: SPEED_BOOST  │                │ - type: SPEED_BOOST  │
  │ - render logic ───┐  │                │ - (no render logic)  │──┐
  │ - color: GREEN    │  │                └──────────────────────┘  │
  └───────────────────┼──┘                                          │
                      │                                             │
  ┌───────────────────┼──┐                ┌──────────────────────┐  │
  │ PowerUp 2         │  │                │ PowerUp 2            │  │
  │ - position (200,150) │                │ - position (200,150) │  │
  │ - size: 20        │  │                │ - size: 20           │  │
  │ - type: SPEED_BOOST  │                │ - type: SPEED_BOOST  │  │share
  │ - render logic ───┼──│ DUPLICATE     │ - (no render logic)  │──┤
  │ - color: GREEN    │  │                └──────────────────────┘  │
  └───────────────────┼──┘                                          │
                      │                                             │
  ┌───────────────────┼──┐                ┌──────────────────────┐  │
  │ PowerUp 3         │  │                │ PowerUp 3            │  │
  │ - position (300,200) │                │ - position (300,200) │  │
  │ - size: 20        │  │                │ - size: 20           │  │
  │ - type: SPEED_BOOST  │                │ - type: SPEED_BOOST  │  │
  │ - render logic ───┼──│ DUPLICATE     │ - (no render logic)  │──┘
  │ - color: GREEN    │  │                └──────────────────────┘
  └───────────────────┴──┘                          │
                                                    │ all reference
         Memory: HIGH                               ▼
         (3 × full objects)              ┌──────────────────────┐
                                         │ ConcreteFlyweight    │
                                         │ (SPEED_BOOST)        │
                                         │                      │
                                         │ - type: SPEED_BOOST  │
                                         │ - color: GREEN       │
                                         │ - render logic       │
                                         │   (SINGLE COPY)      │
                                         └──────────────────────┘

                                                  Memory: LOW
                                         (3 lightweight + 1 flyweight)
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

## Integration with Power-Up Inventory System

The Flyweight pattern works alongside the **Power-Up Inventory System**:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│           FLYWEIGHT + POWER-UP INVENTORY INTEGRATION                        │
└─────────────────────────────────────────────────────────────────────────────┘

  ┌──────────────────┐                    ┌──────────────────────────────────┐
  │ PowerUp (Map)    │                    │ PowerUpInventory (Player)        │
  │ Uses Flyweight   │                    │ Stores collected power-ups       │
  │ for rendering    │                    │                                  │
  │                  │   on collision     │ ┌────┬────┬────┬────┐            │
  │ - position       │ ─────────────────▶ │ │ F1 │ F2 │ F3 │ F4 │            │
  │ - type           │   store, don't     │ │SPD │DMG │SHD │AMO │            │
  │ - render() ──┐   │   apply instantly  │ └────┴────┴────┴────┘            │
  └──────────────┼───┘                    └──────────────────────────────────┘
                 │                                      │
                 │                                      │ F1-F4 pressed
                 ▼                                      ▼
  ┌──────────────────────┐                ┌──────────────────────────────────┐
  │ FlyweightFactory     │                │ Player.usePowerUpFromSlot()      │
  │ (Shared rendering)   │                │ - applySpeedBoost()              │
  │                      │                │ - applyDamageBoost()             │
  │ ┌────────────────┐   │                │ - applyShield()                  │
  │ │ SPEED_BOOST    │   │                │ - applyAmmoRefill()              │
  │ │ ConcreteFly    │   │                └──────────────────────────────────┘
  │ └────────────────┘   │
  │ ┌────────────────┐   │
  │ │ DAMAGE_BOOST   │   │       PowerUpType enum shared by:
  │ │ ConcreteFly    │   │       ├── ConcreteFlyweight (intrinsic state)
  │ └────────────────┘   │       ├── PowerUpInventory (stored slots)
  │ ┌────────────────┐   │       └── Player effect methods
  │ │ SHIELD         │   │
  │ │ ConcreteFly    │   │
  │ └────────────────┘   │
  │ ┌────────────────┐   │
  │ │ AMMO_REFILL    │   │
  │ │ ConcreteFly    │   │
  │ └────────────────┘   │
  └──────────────────────┘
```

### Relationship Between Patterns

| Component           | Flyweight Role                 | Inventory Role                        |
| ------------------- | ------------------------------ | ------------------------------------- |
| `PowerUpType` enum  | Intrinsic state (shared)       | Identifies stored power-up type       |
| `PowerUp` (client)  | Client holding extrinsic state | Visual representation on map          |
| `PowerUp` (server)  | Contains type and duration     | Source of collected power-up data     |
| `ConcreteFlyweight` | Shared rendering per type      | N/A (rendering only)                  |
| `PowerUpInventory`  | N/A (storage only)             | Stores up to 4 power-ups for use      |
| `Player`            | Collides with map PowerUps     | Owns inventory, uses stored power-ups |

### Data Flow: Collection to Usage

```
1. PowerUp spawns on map → Rendered using Flyweight pattern
2. Player collides with PowerUp → Stored in PowerUpInventory (not applied)
3. Player presses F1-F4 → UsePowerUpMessage sent to server
4. Server applies effect → Player gains boost/shield/ammo
5. PowerUp removed from map → Flyweight still cached for next spawn
```

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
