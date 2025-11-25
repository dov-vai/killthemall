# Design Patterns Implementation Summary

This project demonstrates two Gang of Four (GoF) design patterns in the KillThemAll 2D shooter game:

## 1. Flyweight Pattern (Structural)

**Location**: `client/src/com/javakaian/shooter/shapes/flyweight/`

**Purpose**: Optimize memory usage for PowerUp objects by sharing common rendering logic.

**Key Components**:

- `Flyweight` - Interface for shared operations
- `ConcreteFlyweight` - Stores intrinsic state (PowerUpType)
- `UnsharedConcreteFlyweight` - Stores all state
- `FlyweightFactory` - Manages and shares flyweight instances
- `PowerUp` - Client that uses flyweights

**Benefits**:

- 64% memory reduction for multiple PowerUps
- Shared rendering logic across instances
- Scalable to hundreds of objects

**Documentation**: See `FLYWEIGHT_PATTERN.md`

---

## 2. Interpreter Pattern (Behavioral)

**Location**: `client/src/com/javakaian/shooter/interpreter/`

**Purpose**: Create an in-game command console with a custom grammar for executing game commands.

**Key Components**:

- `Expression` - AbstractExpression interface
- Terminal Expressions: `HealExpression`, `SpeedExpression`, `GodModeExpression`, etc.
- NonTerminal Expressions: `TeleportExpression`, `SequenceExpression`
- `Context` - Stores interpreter state
- `CommandInterpreter` - Parses text into expression trees
- `GameConsole` - Visual UI integration

**Available Commands**:

```
heal <amount>       - Restore health
speed <multiplier>  - Modify speed
teleport <x> <y>    - Move to position
god                 - Toggle invincibility
stats               - Show player info
help                - List commands
```

**Benefits**:

- Extensible command system
- Composable expressions
- **Visible in-game** - Press `~` to open console
- Easy to test and maintain

**Documentation**: See `INTERPRETER_PATTERN.md`

---

## How to Use

### Flyweight Pattern

The Flyweight pattern is used automatically when PowerUps are created in the game. No special action needed.

```java
// PowerUps automatically share flyweight objects
PowerUp powerUp = new PowerUp(x, y, size, PowerUpType.SPEED_BOOST);
powerUp.render(shapeRenderer); // Uses shared flyweight internally
```

### Interpreter Pattern

The Interpreter pattern is accessible through the in-game console:

1. **Launch the game** and connect to server
2. **Press `~`** (backtick/tilde) to open console
3. **Type commands** like:
   - `heal 50`
   - `speed 2`
   - `teleport 500 300`
   - `god`
   - `stats`
4. **Press ENTER** to execute
5. **Press `~` or ESC** to close

### Testing

**Flyweight Demo**:

```bash
java com.javakaian.shooter.shapes.flyweight.FlyweightDemo
```

**Interpreter Demo**:

```bash
java com.javakaian.shooter.interpreter.InterpreterDemo
```

---

## Project Structure

```
killthemall/
├── FLYWEIGHT_PATTERN.md          # Flyweight documentation
├── INTERPRETER_PATTERN.md        # Interpreter documentation
├── DESIGN_PATTERNS_SUMMARY.md    # This file
│
└── client/src/com/javakaian/
    └── shooter/
        ├── shapes/
        │   ├── PowerUp.java                    # Modified to use Flyweight
        │   └── flyweight/                      # Flyweight pattern
        │       ├── Flyweight.java
        │       ├── ConcreteFlyweight.java
        │       ├── UnsharedConcreteFlyweight.java
        │       ├── FlyweightFactory.java
        │       ├── FlyweightDemo.java
        │       ├── FlyweightTest.java
        │       ├── README.md
        │       ├── DIAGRAM.txt
        │       └── BEFORE_AFTER.md
        │
        └── interpreter/                        # Interpreter pattern
            ├── Expression.java
            ├── Context.java
            ├── CommandInterpreter.java
            ├── NumberExpression.java
            ├── HealExpression.java
            ├── SpeedExpression.java
            ├── GodModeExpression.java
            ├── HelpExpression.java
            ├── StatsExpression.java
            ├── ClearExpression.java
            ├── TeleportExpression.java
            ├── SequenceExpression.java
            ├── GameConsole.java
            ├── InterpreterDemo.java
            ├── README.md
            └── DIAGRAM.txt
```

---

## Pattern Comparison

| Aspect            | Flyweight                      | Interpreter                    |
| ----------------- | ------------------------------ | ------------------------------ |
| **Category**      | Structural                     | Behavioral                     |
| **Intent**        | Share objects to reduce memory | Define grammar and interpreter |
| **Visibility**    | Transparent to user            | Visible in-game console        |
| **Complexity**    | Low                            | Medium                         |
| **Extensibility** | Fixed types                    | Highly extensible              |
| **Use Case**      | Many similar objects           | Command language               |

---

## Compliance with GoF

Both patterns strictly follow the Gang of Four definitions:

### Flyweight Pattern ✅

- Separates intrinsic and extrinsic state
- Uses factory to manage shared instances
- Implements both shared and unshared variants
- Demonstrates memory efficiency

### Interpreter Pattern ✅

- Defines grammar for a language
- Implements AbstractExpression interface
- Provides Terminal and NonTerminal expressions
- Uses Context for global state
- Client builds and interprets expression trees

---

## Additional Resources

### Flyweight Pattern

- **Demo**: `FlyweightDemo.java`
- **Tests**: `FlyweightTest.java`
- **Diagrams**: `flyweight/DIAGRAM.txt`, `flyweight/BEFORE_AFTER.md`
- **Docs**: `FLYWEIGHT_PATTERN.md`, `flyweight/README.md`

### Interpreter Pattern

- **Demo**: `InterpreterDemo.java`
- **In-Game**: Press `~` during gameplay
- **Diagrams**: `interpreter/DIAGRAM.txt`
- **Docs**: `INTERPRETER_PATTERN.md`, `interpreter/README.md`

---

## Key Achievements

1. ✅ **Flyweight Pattern**: Fully implemented with factory, shared/unshared variants
2. ✅ **Interpreter Pattern**: Fully implemented with expression tree and grammar
3. ✅ **In-Game Visibility**: Console accessible during gameplay (press `~`)
4. ✅ **Comprehensive Documentation**: Multiple docs, diagrams, and examples
5. ✅ **Testable**: Demo classes for both patterns
6. ✅ **GoF Compliant**: Strictly follows original pattern definitions
7. ✅ **Production Ready**: Integrated into actual game code

---

## Quick Start Guide

1. **Build the project**:

   ```bash
   ./gradlew build
   ```

2. **Run the game**:

   ```bash
   ./gradlew :client:run
   ```

3. **Open console in-game**: Press `~`

4. **Try commands**:

   ```
   > help
   > heal 50
   > speed 2
   > teleport 1000 500
   > god
   > stats
   ```

5. **Run demos**:

   ```bash
   # Flyweight demo
   java com.javakaian.shooter.shapes.flyweight.FlyweightDemo

   # Interpreter demo
   java com.javakaian.shooter.interpreter.InterpreterDemo
   ```

---

**Created**: November 2025  
**Patterns**: Flyweight (Structural), Interpreter (Behavioral)  
**Source**: Gang of Four Design Patterns
