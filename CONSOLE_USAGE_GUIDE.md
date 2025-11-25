# In-Game Console Usage Guide

## Opening the Console

**Press the `~` key** (backtick/tilde, usually located below ESC) during gameplay.

```
┌─────────────────────────────────────────────────────────────────┐
│                     GAME SCREEN                                 │
│                                                                 │
│    Player: ▪                     Enemy: ○                      │
│                                                                 │
│    Health: 75/100                                               │
│    Weapon: Assault Rifle                                        │
│                                                                 │
│                                                                 │
│                   ← Press ~ key here                            │
└─────────────────────────────────────────────────────────────────┘

                            ▼ Console Opens ▼

┌─────────────────────────────────────────────────────────────────┐
│                     GAME SCREEN                                 │
│                                                                 │
│    Player: ▪                     Enemy: ○                      │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│ === Game Console Ready ===                                      │
│ Type 'help' for available commands                              │
│ > _                                                              │
│                                                                 │
│ Press ~ to toggle console | ESC to close | Type 'help' for cmds│
└─────────────────────────────────────────────────────────────────┘
```

## Basic Commands

### 1. Get Help

```
> help
=== Available Commands ===
heal <amount>    - Restore health points
speed <mult>     - Set speed multiplier
god              - Toggle god mode
stats            - Show player statistics
teleport <x> <y> - Teleport to position
help             - Show this help message
clear            - Clear console output
```

### 2. Heal Player

```
> heal 50
Healed 50 HP. Current health: 100
```

### 3. Increase Speed

```
> speed 3
Speed multiplier set to 3x (Speed: 600)
```

### 4. Teleport

```
> teleport 1000 500
Teleported to (1000, 500)
```

### 5. Toggle God Mode

```
> god
God mode ENABLED - Invincibility activated!

> god
God mode DISABLED
```

### 6. View Statistics

```
> stats
=== Player Statistics ===
Health: 100/100
Position: (1000, 500)
Speed: 600
God Mode: ACTIVE
```

### 7. Clear Console

```
> clear
[Console cleared]
```

## Advanced Usage

### Command Sequences

Execute multiple commands at once using semicolons:

```
> heal 50; speed 2; stats
Healed 50 HP. Current health: 100
Speed multiplier set to 2x (Speed: 400)
=== Player Statistics ===
Health: 100/100
Position: (450, 320)
Speed: 400
```

### Common Patterns

**Quick Setup**:

```
> god; heal 100; speed 2
God mode ENABLED - Invincibility activated!
Healed 100 HP. Current health: 100
Speed multiplier set to 2x (Speed: 400)
```

**Navigation**:

```
> teleport 0 0; stats
Teleported to (0, 0)
=== Player Statistics ===
Health: 100/100
Position: (0, 0)
```

**Testing**:

```
> heal 25; teleport 500 300; speed 3; stats
[Shows results of each command]
```

## Console UI Layout

```
┌──────────────────────── CONSOLE ──────────────────────────────┐
│ Command History (scrolls automatically):                      │
│                                                               │
│ > help                                                        │
│ === Available Commands ===                                    │
│ heal <amount>    - Restore health points                      │
│ ...                                                           │
│                                                               │
│ > heal 50                                                     │
│ Healed 50 HP. Current health: 100                             │
│                                                               │
│ > speed 2                                                     │
│ Speed multiplier set to 2x (Speed: 400)                       │
│                                                               │
│ Current Input Line:                                           │
│ > _                                ← Your typing appears here │
│                                                               │
│ Help Text:                                                    │
│ Press ~ to toggle console | ESC to close | Type 'help' ...   │
└───────────────────────────────────────────────────────────────┘
```

## Keyboard Controls

| Key                 | Action                |
| ------------------- | --------------------- |
| **~**               | Toggle console on/off |
| **ESC**             | Close console         |
| **ENTER**           | Execute command       |
| **BACKSPACE**       | Delete character      |
| **Letters/Numbers** | Type command          |

## Example Session

```
=== Game Console Ready ===
Type 'help' for available commands

> heal 30
Healed 30 HP. Current health: 80

> stats
=== Player Statistics ===
Health: 80/100
Position: (245, 178)

> teleport 1000 1000
Teleported to (1000, 1000)

> speed 4
Speed multiplier set to 4x (Speed: 800)

> god
God mode ENABLED - Invincibility activated!

> stats
=== Player Statistics ===
Health: 80/100
Position: (1000, 1000)
Speed: 800
God Mode: ACTIVE

> heal 20
Healed 20 HP. Current health: 100

> clear
[Console output cleared]
```

## Tips & Tricks

### 1. Quick Commands

Short aliases work for some commands:

- `tp` = `teleport`

### 2. Error Messages

Invalid commands show helpful error messages:

```
> heal
Error: heal requires an amount. Usage: heal <amount>

> invalid
Error: Unknown command: invalid. Type 'help' for available commands.

> teleport 100
Error: teleport requires X and Y coordinates. Usage: teleport <x> <y>
```

### 3. Console While Playing

The console can be opened at any time during gameplay. Game continues while console is open.

### 4. Debugging

Use the console to test game mechanics:

```
> teleport 0 0; heal 100; god
[Test spawn point with full health and god mode]
```

### 5. Speed Testing

```
> speed 1
Speed multiplier set to 1x (Speed: 200)  ← Normal speed

> speed 5
Speed multiplier set to 5x (Speed: 1000) ← Very fast!
```

## Troubleshooting

**Console won't open?**

- Make sure you're in PlayState (actively playing)
- Check that the ~ key is pressed (not Shift+~)
- Player must be logged in

**Commands not working?**

- Check spelling (commands are case-insensitive)
- Verify command syntax with `help`
- Ensure player is initialized

**Console disappeared?**

- Press ~ again to reopen
- Console auto-hides when you press ESC

## Pattern Demonstration

The console demonstrates the **Interpreter Pattern**:

1. **You type**: `heal 50`
2. **Parser creates**: `HealExpression(NumberExpression(50))`
3. **Interpreter executes**: Expression tree is evaluated
4. **Result**: Player healed by 50 HP

This showcases:

- ✅ Grammar parsing
- ✅ Expression tree building
- ✅ Command interpretation
- ✅ Real-time execution

---

**Enjoy experimenting with the in-game console!**

Press `~` and type `help` to get started.
