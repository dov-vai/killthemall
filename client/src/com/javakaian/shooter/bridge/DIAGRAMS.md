# Bridge Pattern - Visual Diagrams

## 1. Pattern Structure Diagram

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                         BRIDGE DESIGN PATTERN                                 │
│                           Two Independent Hierarchies                         │
└──────────────────────────────────────────────────────────────────────────────┘

        ABSTRACTION                           IMPLEMENTATION
         (Controls)                            (Mechanisms)

┌─────────────────────────┐              ┌──────────────────────┐
│   WeaponControl         │              │  FiringMechanism     │
│   (abstract)            │◆─────────────│  (interface)         │
├─────────────────────────┤   Bridge     ├──────────────────────┤
│ - firingMechanism       │   Link       │ + fire()             │
├─────────────────────────┤              │ + reload()           │
│ + execute()             │              │ + canFire()          │
│ + aim()                 │              │ + getFireRate()      │
│ + reload()              │              │ + getAmmoCount()     │
│ + specialAction()       │              │ + update()           │
│ + setFiringMechanism()  │              └──────────────────────┘
└─────────────────────────┘                         △
            △                                       │
            │                                       │
     ┌──────┴───────┐                    ┌─────────┼────────────┐
     │              │                    │         │            │
     │              │                    │         │            │
┌────┴─────┐  ┌────┴──────┐   ┌─────────┴──┐ ┌───┴────┐ ┌─────┴──────┐
│ Manual   │  │ AutoAim   │   │ SemiAuto   │ │ Burst  │ │ FullAuto   │
│ Control  │  │ Control   │   │ Firing     │ │ Firing │ │ Firing     │
└──────────┘  └───────────┘   └────────────┘ └────────┘ └────────────┘

Refined                        Concrete Implementors
Abstractions
```

## 2. Interaction Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    HOW THE BRIDGE PATTERN WORKS                              │
└─────────────────────────────────────────────────────────────────────────────┘

  CLIENT                ABSTRACTION              IMPLEMENTOR
    │                       │                         │
    │   execute(target)     │                         │
    │──────────────────────>│                         │
    │                       │                         │
    │                       │ aim(target)             │
    │                       │────┐                    │
    │                       │    │ (control-specific  │
    │                       │<───┘  aiming logic)     │
    │                       │                         │
    │                       │ fire(pos, dir)          │
    │                       │────────────────────────>│
    │                       │                         │
    │                       │                         │──┐
    │                       │                         │  │ (mechanism-specific
    │                       │                         │<─┘  firing logic)
    │                       │<────────────────────────│
    │<──────────────────────│                         │
    │                       │                         │
```

## 3. Runtime Flexibility Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              BRIDGE PATTERN: RUNTIME SWITCHING CAPABILITY                    │
└─────────────────────────────────────────────────────────────────────────────┘

Initial Setup:
┌──────────────┐          ┌──────────────┐
│ Manual       │◆────────>│ SemiAuto     │
│ Control      │          │ Firing       │
└──────────────┘          └──────────────┘

After setFiringMechanism(new BurstFiring()):
┌──────────────┐          ┌──────────────┐
│ Manual       │    ╳     │ SemiAuto     │
│ Control      │          │ Firing       │
└──────────────┘          └──────────────┘
       │◆
       │
       └───────────────────>┌──────────────┐
                            │ Burst        │
                            │ Firing       │
                            └──────────────┘

After new AutoAimControl(new FullAutoFiring()):
┌──────────────┐          ┌──────────────┐
│ AutoAim      │◆────────>│ FullAuto     │
│ Control      │          │ Firing       │
└──────────────┘          └──────────────┘
```

## 4. Class Explosion Comparison

```
┌─────────────────────────────────────────────────────────────────────────────┐
│            WITHOUT BRIDGE PATTERN (Inheritance Hell)                         │
└─────────────────────────────────────────────────────────────────────────────┘

                        WeaponController
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
  ManualWeapon          AutoAimWeapon          GamepadWeapon
        │                      │                      │
    ┌───┼───┐              ┌───┼───┐              ┌───┼───┐
    │   │   │              │   │   │              │   │   │
Manual Manual Manual   AutoAim AutoAim AutoAim  Gamepad Gamepad Gamepad
Semi   Burst  Full      Semi   Burst   Full      Semi    Burst   Full

TOTAL: 9 classes for 3 controls × 3 mechanisms
PROBLEM: Adding 1 control or mechanism adds 3+ new classes!

┌─────────────────────────────────────────────────────────────────────────────┐
│              WITH BRIDGE PATTERN (Clean Separation)                          │
└─────────────────────────────────────────────────────────────────────────────┘

Control Hierarchy:          Firing Hierarchy:
┌──────────────┐           ┌──────────────┐
│ Manual       │           │ SemiAuto     │
└──────────────┘           └──────────────┘
┌──────────────┐           ┌──────────────┐
│ AutoAim      │           │ Burst        │
└──────────────┘           └──────────────┘
┌──────────────┐           ┌──────────────┐
│ Gamepad      │           │ FullAuto     │
└──────────────┘           └──────────────┘

TOTAL: 6 classes for 3 controls + 3 mechanisms
BENEFIT: Any combination possible! (3 × 3 = 9 combinations)
FLEXIBILITY: Adding 1 control or mechanism adds only 1 class!
```

## 5. Method Call Sequence

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                  TYPICAL OPERATION SEQUENCE                                  │
└─────────────────────────────────────────────────────────────────────────────┘

[Client] creates:
    control = new ManualControl(new SemiAutoFiring())

[Game Loop] update cycle:

    1. control.update(deltaTime)
       │
       └──> firingMechanism.update(deltaTime)
             └──> cooldownTimer -= deltaTime

    2. [User Input] Mouse Click
       │
       ├──> control.aim(mousePos, player)
       │    └──> Calculate aim direction (Manual: direct)
       │
       └──> control.execute(mousePos, player)
            └──> aimDir = aim(mousePos, player)
            └──> firingMechanism.fire(playerPos, aimDir, player)
                 └──> if (canFire())
                      └──> Create bullet
                      └──> currentAmmo--
                      └──> cooldownTimer = cooldownDuration

    3. [User Input] 'R' key
       │
       └──> control.reload()
            └──> firingMechanism.reload()
                 └──> currentAmmo = maxAmmo

    4. [User Input] Special key
       │
       └──> control.specialAction()
            └──> Manual: toggle precisionMode
                 AutoAim: lock/unlock target
```

## 6. Component Responsibilities

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     WHO DOES WHAT?                                           │
└─────────────────────────────────────────────────────────────────────────────┘

WeaponControl (Abstraction)
├─ Owns FiringMechanism reference (the bridge)
├─ Defines interface for control operations
├─ Handles aim direction calculation
├─ Manages control-specific state
└─ Delegates firing to FiringMechanism

ManualControl
├─ Implements direct aiming (no assistance)
├─ Manages precision mode toggle
└─ Uses inherited bridge to fire

AutoAimControl
├─ Implements auto-aim (snap to enemies)
├─ Manages target locking
└─ Uses inherited bridge to fire

FiringMechanism (Implementor)
├─ Defines interface for firing operations
├─ Manages ammunition
├─ Manages fire rate and cooldowns
└─ Independent of control mode

SemiAutoFiring
├─ Fires single bullet per trigger
├─ Moderate fire rate
└─ Simple, reliable firing

BurstFiring
├─ Fires 3-round bursts
├─ Adds spread to bullets
└─ Configurable burst size

FullAutoFiring
├─ Fires continuously
├─ Accumulates accuracy penalty
└─ Recovers accuracy when not firing
```

## 7. Extension Points

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   HOW TO EXTEND THE PATTERN                                  │
└─────────────────────────────────────────────────────────────────────────────┘

ADD NEW CONTROL MODE:
┌────────────────────────────────────┐
│ public class TouchScreenControl    │
│        extends WeaponControl {     │
│                                    │
│   // Implement aim() with          │
│   // touch-specific logic          │
│                                    │
│   // Works with ALL existing       │
│   // firing mechanisms!            │
└────────────────────────────────────┘

ADD NEW FIRING MECHANISM:
┌────────────────────────────────────┐
│ public class ChargedShotFiring     │
│    implements FiringMechanism {    │
│                                    │
│   // Implement fire() with         │
│   // charging logic                │
│                                    │
│   // Works with ALL existing       │
│   // control modes!                │
└────────────────────────────────────┘

COMBINE THEM:
┌────────────────────────────────────┐
│ WeaponControl control =            │
│   new TouchScreenControl(          │
│     new ChargedShotFiring());      │
│                                    │
│ // New features work together!     │
└────────────────────────────────────┘
```

## 8. Real-World Game Integration

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    INTEGRATION WITH PLAYSTATE                                │
└─────────────────────────────────────────────────────────────────────────────┘

PlayState {
    WeaponControl weaponControl;

    init() {
        weaponControl = new ManualControl(new SemiAutoFiring());
    }

    handleInput() {
        // Fire
        if (mouse.leftClick) {
            weaponControl.execute(mousePos, player);
        }

        // Switch firing mode
        if (key.NUM_1) weaponControl.setFiringMechanism(new SemiAutoFiring());
        if (key.NUM_2) weaponControl.setFiringMechanism(new BurstFiring());
        if (key.NUM_3) weaponControl.setFiringMechanism(new FullAutoFiring());

        // Switch control mode
        if (key.C) {
            FiringMechanism current = weaponControl.getFiringMechanism();
            weaponControl = new AutoAimControl(current); // Keep firing mode!
        }

        // Special action
        if (key.SPACE) weaponControl.specialAction();

        // Reload
        if (key.R) weaponControl.reload();
    }

    update(deltaTime) {
        weaponControl.update(deltaTime);
    }
}
```

---

**Legend:**

- `◆` = Composition (has-a relationship / bridge link)
- `△` = Inheritance (is-a relationship)
- `──>` = Method call / dependency
- `╳` = Disconnected / removed reference
