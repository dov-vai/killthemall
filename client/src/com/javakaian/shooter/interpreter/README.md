# Interpreter Pattern - Quick Reference

## Files Created

### Core Pattern Implementation

1. **`Expression.java`** - AbstractExpression interface
2. **`Context.java`** - Stores interpreter state and variables
3. **`CommandInterpreter.java`** - Client that parses text into expression tree

### Terminal Expressions (Leaf Nodes)

4. **`NumberExpression.java`** - Represents integer literals
5. **`HealExpression.java`** - Heal command
6. **`SpeedExpression.java`** - Speed boost command
7. **`GodModeExpression.java`** - God mode toggle
8. **`HelpExpression.java`** - Display help
9. **`StatsExpression.java`** - Show statistics
10. **`ClearExpression.java`** - Clear console

### NonTerminal Expressions (Composite Nodes)

11. **`TeleportExpression.java`** - Teleport with X,Y coordinates
12. **`SequenceExpression.java`** - Execute multiple commands

### UI Integration

13. **`GameConsole.java`** - Visual console UI component
14. **`InterpreterDemo.java`** - Demonstration and testing

### Modified Files

- **`PlayState.java`** - Integrated console into game loop

### Documentation

- **`INTERPRETER_PATTERN.md`** - Comprehensive documentation
- **`README.md`** - This quick reference

## Location

```
client/src/com/javakaian/shooter/interpreter/
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
└── README.md
```

## Quick Start

### In-Game Usage

1. **Launch the game** and join a server
2. **Press `~`** (backtick/tilde) to open console
3. **Type a command**, e.g., `heal 50`
4. **Press ENTER** to execute
5. **Press `~` or ESC** to close console

### Available Commands

```
heal 50              - Restore 50 HP
speed 2              - Double movement speed
teleport 500 300     - Move to position (500, 300)
god                  - Toggle invincibility
stats                - Show player info
help                 - List all commands
clear                - Clear console output
```

### Command Sequences

Execute multiple commands:

```
heal 50; speed 2; stats
god; teleport 1000 500; heal 100
```

## Pattern Structure

### Expression Tree Example

**Input**: `teleport 500 300`

**Expression Tree**:

```
     TeleportExpression
         /          \
NumberExpression  NumberExpression
      (500)           (300)
```

**Execution Flow**:

1. `CommandInterpreter` parses "teleport 500 300"
2. Creates `TeleportExpression` with two `NumberExpression` children
3. `interpret(context)` is called on root
4. Each `NumberExpression` evaluates and stores value in context
5. `TeleportExpression` retrieves values and moves player

## Key Classes

### Expression (Interface)

```java
public interface Expression {
    boolean interpret(Context context);
}
```

### Context

```java
public class Context {
    private Map<String, Object> variables;
    private PlayState playState;
    private Player player;
    // ...
}
```

### CommandInterpreter

```java
public class CommandInterpreter {
    public Expression parse(String commandText) {
        // Parses text and builds expression tree
    }
}
```

## Testing

### Run Demo

```bash
java com.javakaian.shooter.interpreter.InterpreterDemo
```

### Programmatic Usage

```java
Context context = new Context(playState, player);
CommandInterpreter interpreter = new CommandInterpreter();

Expression expr = interpreter.parse("heal 50");
boolean success = expr.interpret(context);
String output = context.getOutput();
```

## Pattern Components

| Component                 | Role            | Examples                                   |
| ------------------------- | --------------- | ------------------------------------------ |
| **AbstractExpression**    | Interface       | `Expression`                               |
| **TerminalExpression**    | Leaf nodes      | `NumberExpression`, `HealExpression`       |
| **NonterminalExpression** | Composite nodes | `TeleportExpression`, `SequenceExpression` |
| **Context**               | Global state    | `Context`                                  |
| **Client**                | Parser          | `CommandInterpreter`                       |

## Benefits

✅ Easy to extend with new commands  
✅ Composable expressions  
✅ Testable components  
✅ Clear separation of parsing and execution  
✅ Visible in-game (console UI)

## Adding New Commands

1. **Create Expression class**:

```java
public class MyCommandExpression implements Expression {
    @Override
    public boolean interpret(Context context) {
        // Implementation
        context.appendOutput("Command executed!");
        return true;
    }
}
```

2. **Add to CommandInterpreter**:

```java
case "mycommand":
    return new MyCommandExpression();
```

3. **Update help text** in `HelpExpression`

## Integration Points

### PlayState Integration

- Console initialized in `loginReceived()`
- Updated in `update()` method
- Rendered in `render()` method

### Console Toggle

- Press `~` key to toggle visibility
- Console captures input when visible
- ESC key closes console

## Compliance

✅ Follows GoF Interpreter pattern structure  
✅ Implements AbstractExpression interface  
✅ Separates Terminal and NonTerminal expressions  
✅ Uses Context for state management  
✅ Client builds and interprets expression trees  
✅ **Visible and usable in-game** ✨
