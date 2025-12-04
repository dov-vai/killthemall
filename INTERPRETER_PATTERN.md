# Interpreter Design Pattern Implementation

## Overview

This implementation demonstrates the **Interpreter Design Pattern** (GoF) in the KillThemAll 2D shooter game. The pattern is applied to create an in-game command console that allows players to execute commands in real-time during gameplay.

## Pattern Structure

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         INTERPRETER PATTERN STRUCTURE                           │
│                    Integration with Existing Game Classes                       │
└─────────────────────────────────────────────────────────────────────────────────┘

                              EXISTING GAME CLASSES
    ┌──────────────────────────────────────────────────────────────────────────┐
    │                                                                          │
    │  ┌─────────────────────┐      ┌──────────────────┐                      │
    │  │ StateController     │      │ State            │                      │
    │  │ (existing)          │─────▶│ (existing)       │                      │
    │  │                     │      │ - camera         │                      │
    │  │ +setState()         │      │ - sr: ShapeRend  │                      │
    │  │ +render()           │      │ - sb: SpriteBatch│                      │
    │  └─────────────────────┘      └────────┬─────────┘                      │
    │                                        │ extends                         │
    │                                        ▼                                 │
    │  ┌─────────────────────────────────────────────────────────────────┐    │
    │  │ PlayState (existing - modified)                                 │    │
    │  │                                                                 │    │
    │  │ - player: Player              - enemies: List<Enemy>            │    │
    │  │ - bullets: List<Bullet>       - powerUps: List<PowerUp>         │    │
    │  │ - spikes: List<Spike>         - client: OClient                 │    │
    │  │ - healthFont: BitmapFont      - gameConsole: GameConsole ◄──NEW │    │
    │  │                                                                 │    │
    │  │ +render()     ──────────────▶ gameConsole.render()              │    │
    │  │ +update()     ──────────────▶ gameConsole.update()              │    │
    │  │ +loginReceived() ───────────▶ initializes gameConsole           │    │
    │  └──────────────────────────────────────┬──────────────────────────┘    │
    │                                         │                                │
    │                                         │ references                     │
    │                                         ▼                                │
    │  ┌─────────────────────┐      ┌──────────────────┐                      │
    │  │ Player              │      │ Vector2          │                      │
    │  │ (existing)          │      │ (libgdx)         │                      │
    │  │                     │      │ - x: float       │                      │
    │  │ - position: Vector2 │◄────▶│ - y: float       │                      │
    │  │ - health: int       │      └──────────────────┘                      │
    │  │ - id: int           │                                                 │
    │  │ - hasShield: bool   │      ┌──────────────────┐                      │
    │  │                     │      │ BitmapFont       │                      │
    │  │ +setPosition()      │      │ (libgdx)         │                      │
    │  │ +setHealth()        │      │ - for console UI │                      │
    │  │ +getHealth()        │      └──────────────────┘                      │
    │  └─────────────────────┘                                                 │
    │                                                                          │
    └──────────────────────────────────────────────────────────────────────────┘
                  │
                  │ integrates with
                  ▼
    ┌─────────────────────────────────────────────────────────────────────────┐
    │                         INTERPRETER PATTERN                             │
    │                                                                         │
    │           ┌───────────────────┐                                         │
    │           │ CommandInterpreter│ ◄─── Client                             │
    │           │ +parse(String)    │      (Parses text into expression tree) │
    │           └────────┬──────────┘                                         │
    │                    │ creates                                            │
    │                    ▼                                                    │
    │           ┌────────────────┐                                            │
    │           │  <<interface>> │                                            │
    │           │   Expression   │ ◄─── AbstractExpression                    │
    │           │                │                                            │
    │           │ +interpret(    │                                            │
    │           │    Context)    │                                            │
    │           └───────┬────────┘                                            │
    │                   │                                                     │
    │        ┌──────────┴──────────────────────────┐                          │
    │        │                                     │                          │
    │        │                                     │                          │
    │ ┌──────▼───────────┐              ┌─────────▼─────────────┐             │
    │ │TerminalExpression│              │ NonterminalExpression │             │
    │ │                  │              │                       │             │
    │ │ - NumberExpr     │              │ - TeleportExpression  │             │
    │ │ - HealExpression │              │ - SequenceExpression  │             │
    │ │ - SpeedExpression│              │                       │             │
    │ │ - GodModeExpr    │              │   Contains other      │             │
    │ │ - HelpExpression │              │   expressions         │             │
    │ │ - StatsExpression│              │                       │             │
    │ │ - ClearExpression│              │                       │             │
    │ └──────────────────┘              └───────────────────────┘             │
    │        │                                     │                          │
    │        └──────────┬──────────────────────────┘                          │
    │                   │ uses                                                │
    │                   ▼                                                     │
    │           ┌───────────────┐                                             │
    │           │   Context     │                                             │
    │           │───────────────│                                             │
    │           │ - variables   │                                             │
    │           │ - playState ──┼────────▶ PlayState (existing)               │
    │           │ - player ─────┼────────▶ Player (existing)                  │
    │           │ - output      │                                             │
    │           └───────────────┘                                             │
    │                                                                         │
    │           ┌───────────────────────────────────────────────────┐         │
    │           │ GameConsole (UI Component)                        │         │
    │           │                                                   │         │
    │           │ - interpreter: CommandInterpreter                 │         │
    │           │ - context: Context                                │         │
    │           │ - visible: boolean                                │         │
    │           │ - font: BitmapFont ◄───────── from libgdx         │         │
    │           │                                                   │         │
    │           │ +toggle()  ◄─────── triggered by ~ key press      │         │
    │           │ +render()  ◄─────── called by PlayState.render()  │         │
    │           │ +update()  ◄─────── called by PlayState.update()  │         │
    │           └───────────────────────────────────────────────────┘         │
    │                                                                         │
    └─────────────────────────────────────────────────────────────────────────┘
                  │
                  │ uses for rendering
                  ▼
    ┌──────────────────────────────────────────────────────────────────────────┐
    │                         LIBGDX FRAMEWORK                                 │
    │                                                                          │
    │  ┌─────────────────────┐      ┌──────────────────┐                      │
    │  │ ShapeRenderer       │      │ SpriteBatch      │                      │
    │  │ (libgdx)            │      │ (libgdx)         │                      │
    │  │                     │      │                  │                      │
    │  │ +setColor()         │      │ +begin()         │                      │
    │  │ +rect()             │      │ +end()           │                      │
    │  │ +rectLine()         │      │ +draw()          │                      │
    │  └─────────────────────┘      └──────────────────┘                      │
    │           ▲                          ▲                                   │
    │           │                          │                                   │
    │           │ console background       │ console text                      │
    │           │                          │                                   │
    │  ┌────────┴──────────────────────────┴────────┐                         │
    │  │              GameConsole.render()          │                         │
    │  │              GameConsole.renderText()      │                         │
    │  └────────────────────────────────────────────┘                         │
    │                                                                          │
    │  ┌─────────────────────┐      ┌──────────────────┐                      │
    │  │ Gdx.input           │      │ Input.Keys       │                      │
    │  │ (libgdx)            │      │ (libgdx)         │                      │
    │  │                     │      │                  │                      │
    │  │ +isKeyJustPressed() │      │ Keys.GRAVE (~)   │                      │
    │  │                     │      │ Keys.ENTER       │                      │
    │  │                     │      │ Keys.ESCAPE      │                      │
    │  └─────────────────────┘      └──────────────────┘                      │
    │                                                                          │
    └──────────────────────────────────────────────────────────────────────────┘
```

## Class Relationships Table

| Existing Class      | Role in Pattern      | Connection                                       |
| ------------------- | -------------------- | ------------------------------------------------ |
| `PlayState`         | Host for GameConsole | Initializes, updates, and renders console        |
| `Player`            | Target of commands   | HealExpression, TeleportExpression modify Player |
| `Vector2`           | Position data        | Used by TeleportExpression to set position       |
| `StateController`   | State management     | Controls PlayState lifecycle                     |
| `State`             | Base class           | PlayState inherits rendering infrastructure      |
| `ShapeRenderer`     | Console background   | GameConsole uses for semi-transparent bg         |
| `SpriteBatch`       | Text rendering       | GameConsole uses for command/output text         |
| `BitmapFont`        | Font rendering       | Console displays text using BitmapFont           |
| `Gdx.input`         | Keyboard input       | Detects ~ key to toggle console                  |
| `Input.Keys`        | Key constants        | GRAVE, ENTER, ESCAPE, BACKSPACE                  |
| `OClient`           | Network client       | Future: could send commands to server            |
| `GameManagerFacade` | Font generation      | Creates BitmapFont for console                   |

## Command to Game Object Mapping

| Command            | Expression         | Affects         | Existing Class                               |
| ------------------ | ------------------ | --------------- | -------------------------------------------- |
| `heal <n>`         | HealExpression     | Player health   | `Player.setHealth()`                         |
| `teleport <x> <y>` | TeleportExpression | Player position | `Player.setPosition(Vector2)`                |
| `speed <n>`        | SpeedExpression    | Movement speed  | Context variable                             |
| `god`              | GodModeExpression  | Invincibility   | Context variable                             |
| `stats`            | StatsExpression    | Display info    | `Player.getHealth()`, `Player.getPosition()` |
| `help`             | HelpExpression     | Show commands   | Console output                               |
| `clear`            | ClearExpression    | Clear console   | Console output buffer                        |

## Execution Flow with Existing Classes

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    EXECUTION FLOW: "heal 50" COMMAND                        │
└─────────────────────────────────────────────────────────────────────────────┘

  User Input                 Pattern Classes              Existing Game Classes
  ──────────                 ───────────────              ─────────────────────

  ┌─────────────┐
  │ Press ~ key │
  └──────┬──────┘
         │
         ▼
  ┌─────────────────┐       ┌─────────────────────┐
  │ Gdx.input       │──────▶│ GameConsole.toggle()│
  │ (libgdx)        │       │                     │
  │ Keys.GRAVE      │       │ visible = true      │
  └─────────────────┘       └─────────────────────┘
         │
         ▼
  ┌─────────────┐
  │ Type:       │
  │ "heal 50"   │
  └──────┬──────┘
         │
         ▼
  ┌─────────────────┐       ┌─────────────────────┐
  │ Press ENTER     │──────▶│ GameConsole         │
  │ Keys.ENTER      │       │ .executeCommand()   │
  └─────────────────┘       └──────────┬──────────┘
                                       │
                                       ▼
                            ┌─────────────────────┐
                            │ CommandInterpreter  │
                            │ .parse("heal 50")   │
                            └──────────┬──────────┘
                                       │
                                       │ creates expression tree
                                       ▼
                            ┌─────────────────────┐
                            │ HealExpression      │
                            │      │              │
                            │ NumberExpression(50)│
                            └──────────┬──────────┘
                                       │
                                       ▼
                            ┌─────────────────────┐      ┌──────────────────┐
                            │ HealExpression      │      │                  │
                            │ .interpret(context) │─────▶│ Context          │
                            │                     │      │ - player ────────┼───┐
                            │ 1. Get amount (50)  │      │ - playState      │   │
                            │ 2. Get player       │      │ - output         │   │
                            │ 3. Calculate health │      └──────────────────┘   │
                            └──────────┬──────────┘                             │
                                       │                                        │
                                       │                                        │
                                       ▼                                        ▼
                                                              ┌──────────────────────┐
                                                              │ Player               │
                                                              │ (existing)           │
                                                              │                      │
                                                              │ health: 50 → 100     │
                                                              │                      │
                                                              │ .setHealth(100)      │
                                                              │ .getHealth() → 100   │
                                                              └──────────────────────┘
                                       │
                                       ▼
                            ┌─────────────────────┐
                            │ context.appendOutput│
                            │ ("Healed 50 HP.    │
                            │  Current: 100")     │
                            └──────────┬──────────┘
                                       │
                                       ▼
                            ┌─────────────────────┐      ┌──────────────────┐
                            │ GameConsole         │      │ SpriteBatch      │
                            │ .renderText()       │─────▶│ (libgdx)         │
                            │                     │      │                  │
                            │ Display output      │      │ draws text       │
                            └─────────────────────┘      └──────────────────┘
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│               EXECUTION FLOW: "teleport 500 300" COMMAND                    │
└─────────────────────────────────────────────────────────────────────────────┘

  Command: "teleport 500 300"

                            ┌─────────────────────┐
                            │ CommandInterpreter  │
                            │ .parse()            │
                            └──────────┬──────────┘
                                       │
                                       │ creates
                                       ▼
                            ┌─────────────────────┐
                            │ TeleportExpression  │
                            │   /           \     │
                            │ Number(500) Number(300)
                            └──────────┬──────────┘
                                       │
                                       │ interpret
                                       ▼
                            ┌─────────────────────┐
                            │ 1. xExpr.interpret()│
                            │    → lastNumber=500 │
                            │                     │
                            │ 2. yExpr.interpret()│
                            │    → lastNumber=300 │
                            └──────────┬──────────┘
                                       │
                                       ▼
                                                              ┌──────────────────────┐
                                                              │ Player               │
                                                              │ (existing)           │
                                                              │                      │
                                                              │ position.x: → 500    │
                                                              │ position.y: → 300    │
                                                              │                      │
                                                              │ .setPosition(        │
                                                              │   new Vector2(500,   │
                                                              │               300))  │
                                                              └──────────────────────┘
                                                                        │
                                                                        ▼
                                                              ┌──────────────────────┐
                                                              │ Vector2              │
                                                              │ (libgdx)             │
                                                              │                      │
                                                              │ x: 500               │
                                                              │ y: 300               │
                                                              └──────────────────────┘
```

## Components

### 1. **Expression Interface** (`Expression.java`)

The AbstractExpression that declares the `interpret(Context)` operation common to all nodes in the abstract syntax tree.

### 2. **Context** (`Context.java`)

Contains global information needed by the interpreter:

- Game state references (`PlayState`, `Player`)
- Variables for storing intermediate values
- Output buffer for command results

### 3. **Terminal Expressions**

Leaf nodes in the expression tree that don't contain other expressions:

- **`NumberExpression`** - Represents integer literals
- **`HealExpression`** - Restores player health
- **`SpeedExpression`** - Modifies player speed
- **`GodModeExpression`** - Toggles invincibility
- **`HelpExpression`** - Displays available commands
- **`StatsExpression`** - Shows player statistics
- **`ClearExpression`** - Clears console output

### 4. **NonTerminal Expressions**

Composite nodes that contain other expressions:

- **`TeleportExpression`** - Composite of two `NumberExpression` (x, y coordinates)
- **`SequenceExpression`** - Executes multiple commands in sequence

### 5. **CommandInterpreter** (`CommandInterpreter.java`)

The Client that parses command strings and builds the expression tree.

### 6. **GameConsole** (`GameConsole.java`)

Visual UI component that integrates the interpreter into the game.

## Grammar

The interpreter supports the following command grammar:

```
<command>   ::= <heal> | <speed> | <god> | <help> | <stats> | <teleport> | <clear>
<sequence>  ::= <command> | <command> ";" <sequence>

<heal>      ::= "heal" <number>
<speed>     ::= "speed" <number>
<teleport>  ::= "teleport" <number> <number>
<god>       ::= "god"
<help>      ::= "help"
<stats>     ::= "stats"
<clear>     ::= "clear"

<number>    ::= [0-9]+
```

## How It Works

### Command Parsing and Execution

1. **User Input**: Player types "heal 50" in the console
2. **Parsing**: `CommandInterpreter.parse("heal 50")` creates:
   ```
   HealExpression
        │
   NumberExpression(50)
   ```
3. **Interpretation**: Expression tree is interpreted with the Context
4. **Execution**: Player's health is restored by 50 points
5. **Output**: "Healed 50 HP. Current health: 100" displayed

### Expression Tree Example

**Simple Command**: `heal 50`

```
     HealExpression
           │
    NumberExpression
         (50)
```

**Composite Command**: `teleport 500 300`

```
     TeleportExpression
         /          \
NumberExpression  NumberExpression
      (500)           (300)
```

**Sequence Command**: `heal 50; speed 2; stats`

```
       SequenceExpression
         /      |        \
   HealExpr  SpeedExpr  StatsExpr
      |         |
   Number(50) Number(2)
```

## Available Commands

| Command      | Syntax               | Description            | Example            |
| ------------ | -------------------- | ---------------------- | ------------------ |
| **heal**     | `heal <amount>`      | Restore health points  | `heal 50`          |
| **speed**    | `speed <multiplier>` | Set speed multiplier   | `speed 2`          |
| **teleport** | `teleport <x> <y>`   | Teleport to position   | `teleport 500 300` |
| **god**      | `god`                | Toggle god mode        | `god`              |
| **stats**    | `stats`              | Show player statistics | `stats`            |
| **help**     | `help`               | Show command list      | `help`             |
| **clear**    | `clear`              | Clear console output   | `clear`            |

### Command Sequences

Execute multiple commands at once using semicolons:

```
heal 50; speed 2; stats
god; heal 100; teleport 400 400
```

## Integration into Game

### In PlayState

The interpreter is integrated into `PlayState.java`:

```java
// Field declaration
private GameConsole gameConsole;

// Initialization (when player logs in)
BitmapFont consoleFont = GameManagerFacade.getInstance()
    .generateBitmapFont(14, Color.GREEN);
gameConsole = new GameConsole(this, player, consoleFont);

// Update loop
if (gameConsole != null) {
    gameConsole.update();
}

// Render loop
if (gameConsole != null) {
    gameConsole.render(sr);
    gameConsole.renderText(sb);
}
```

## Usage In-Game

1. **Open Console**: Press `~` (backtick/tilde) key during gameplay
2. **Type Command**: Enter any command from the list above
3. **Execute**: Press `ENTER` to execute the command
4. **Close Console**: Press `~` again or `ESC`

### Example Session

```
=== Game Console Ready ===
Type 'help' for available commands
> help
=== Available Commands ===
heal <amount>    - Restore health points
speed <mult>     - Set speed multiplier
god              - Toggle god mode
stats            - Show player statistics
teleport <x> <y> - Teleport to position
help             - Show this help message
clear            - Clear console output

> heal 50
Healed 50 HP. Current health: 100

> speed 3
Speed multiplier set to 3x (Speed: 600)

> stats
=== Player Statistics ===
Health: 100/100
Position: (450, 320)
Speed: 600

> teleport 1000 500
Teleported to (1000, 500)

> god
God mode ENABLED - Invincibility activated!
```

## Testing

Run the demo to see the pattern in action:

```bash
# Run the interpreter demo
java com.javakaian.shooter.interpreter.InterpreterDemo
```

**Expected Output**:

```
=== Interpreter Pattern Demonstration ===

Command: help
Output:
=== Available Commands ===
heal <amount>    - Restore health points
speed <mult>     - Set speed multiplier
...
Success: true

Command: heal 50
Output:
Healed 50 HP. Current health: 100
Success: true

...
```

## Key Benefits

1. **Extensibility**: Easy to add new commands by creating new Expression classes
2. **Composability**: Commands can be combined using SequenceExpression
3. **Separation of Concerns**: Parsing logic separate from execution logic
4. **Reusability**: Expressions can be reused in different combinations
5. **Testability**: Each expression can be tested independently

## Pattern Compliance

✅ Implements the Interpreter pattern exactly as defined by Gang of Four
✅ AbstractExpression: `Expression` interface
✅ TerminalExpression: `NumberExpression`, `HealExpression`, etc.
✅ NonterminalExpression: `TeleportExpression`, `SequenceExpression`
✅ Context: Stores global state and variables
✅ Client: `CommandInterpreter` parses and builds expression tree

## Extension Points

### Adding New Commands

1. Create a new Expression class:

```java
public class NewCommandExpression implements Expression {
    @Override
    public boolean interpret(Context context) {
        // Implementation
        return true;
    }
}
```

2. Add parsing logic to `CommandInterpreter`:

```java
case "newcommand":
    return new NewCommandExpression();
```

### Adding Command Parameters

```java
public class ParameterizedExpression implements Expression {
    private Expression paramExpression;

    public ParameterizedExpression(Expression param) {
        this.paramExpression = param;
    }

    @Override
    public boolean interpret(Context context) {
        paramExpression.interpret(context);
        // Use parameter value
        return true;
    }
}
```

## Design Pattern Category

- **Category**: Behavioral Pattern
- **Intent**: Define a representation for a grammar along with an interpreter that uses the representation to interpret sentences in the language
- **Applicability**: Used when there's a language to interpret and sentences can be represented as abstract syntax trees

## References

- **Design Patterns: Elements of Reusable Object-Oriented Software** (Gang of Four)
- Pattern Type: Behavioral
- Also Known As: Little Language
