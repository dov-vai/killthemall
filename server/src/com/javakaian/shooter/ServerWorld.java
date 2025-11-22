package com.javakaian.shooter;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.OServer;
import com.javakaian.network.messages.*;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.factory.BulletFactory;
import com.javakaian.shooter.factory.ConcreteBulletFactory;
import com.javakaian.shooter.factory.BulletType;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.shapes.PowerUp;
import com.javakaian.shooter.shapes.Spike;
import com.javakaian.shooter.shapes.PlacedSpike;
import com.javakaian.shooter.strategy.*;
import com.javakaian.shooter.command.Command;
import com.javakaian.shooter.command.PlaceSpikeCommand;
import com.javakaian.shooter.weapons.Weapon;
import com.javakaian.util.MessageCreator;
import org.apache.log4j.Logger;

import com.javakaian.shooter.builder.WeaponDirector;
import com.javakaian.shooter.weapons.decorators.DamageBoostAttachment;
import com.javakaian.shooter.weapons.decorators.ExtendedMagazineAttachment;
import com.javakaian.shooter.weapons.decorators.GripAttachment;
import com.javakaian.shooter.weapons.decorators.ScopeAttachment;
import com.javakaian.shooter.weapons.decorators.SilencerAttachment;
import com.javakaian.shooter.iterator.*;
import com.javakaian.shooter.iterator.Iterator;

import java.security.SecureRandom;
import java.util.*;

public class ServerWorld implements OMessageListener {

    private List<Player> players;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private List<Spike> spikes;
    private List<PlacedSpike> placedSpikes;

    private OServer server;

    private float deltaTime = 0;

    private float enemyTime = 0f;
    private float spikeSpawnTime = 0f;

    private UserIdPool idPool;

    private Logger logger = Logger.getLogger(ServerWorld.class);

    private BulletFactory bulletFactory;

    // weapon system
    private WeaponDirector weaponDirector;
    private Map<Integer, Weapon> playerWeapons;
    private float gameTime = 0f;

    private EnemyBehaviorStrategy[] behaviorStrategies;
    private int strategyIndex = 0;
    private float strategySwitchTimer = 0f;
    private SecureRandom rng = new SecureRandom();

    // Command pattern for spike placement - stack to support multiple undos
    private Map<Integer, Stack<Command>> playerSpikeCommands;
    // Map KryoNet connection ID -> player ID for cleanup on disconnect
    private Map<Integer, Integer> connectionToPlayerId;

    //iterator
    private PowerUpCollection powerUpsArray; //spwan order
    private PowerUpCollection powerUpsList; //queue
    private PowerUpCollection powerUpsMap; //id for lookup
    private float powerUpSpawnTime = 0f;
    private int powerUpIdCounter = 0;

    public ServerWorld() {

        server = new OServer(this);
        players = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        spikes = new ArrayList<>();
        placedSpikes = new ArrayList<>();

        idPool = new UserIdPool();

        bulletFactory = new ConcreteBulletFactory();

        // weapon system
        weaponDirector = new WeaponDirector();
        playerWeapons = new HashMap<>();

        //iterator
        powerUpsArray = new PowerUpArray();
        powerUpsList = new PowerUpList();
        powerUpsMap = new PowerUpMap();
        
        playerSpikeCommands = new HashMap<>();
        connectionToPlayerId = new HashMap<>();

        behaviorStrategies = new EnemyBehaviorStrategy[]{
                new AggressiveBehavior(),
                new DefensiveBehavior(),
                new FlankingBehavior(),
                new ErraticBehavior()
        };
    }

    public void update(float deltaTime) {

        this.deltaTime = deltaTime;
        this.enemyTime += deltaTime;
        this.gameTime += deltaTime;
        this.strategySwitchTimer += deltaTime;
        this.spikeSpawnTime += deltaTime;
        this.powerUpSpawnTime += deltaTime;

        server.parseMessage();

        // update every object
        players.forEach(p -> p.update(deltaTime));
        enemies.forEach(e -> e.update(deltaTime, players)); // Pass players for AI behavior
        bullets.forEach(b -> b.update(deltaTime));
        spikes.forEach(s -> s.update(deltaTime));
        placedSpikes.forEach(ps -> ps.update(deltaTime));
        players.forEach(p -> p.updatePowerUpEffects(gameTime));

        Iterator<PowerUp> iter = powerUpsArray.createIterator();
        for (iter.first(); !iter.isDone(); iter.next()) {
            PowerUp p = iter.currentItem();
            if (p != null) {
                p.update(deltaTime);
            }
        }   

        checkCollision();

        // update object list. Remove necessary
        players.removeIf(p -> !p.isAlive());
        bullets.removeIf(b -> !b.isVisible());
        spikes.removeIf(s -> !s.isVisible());
        placedSpikes.removeIf(ps -> !ps.isVisible());

        spawnRandomEnemy();
        spawnRandomSpike();
        spawnRandomPowerUp();

        checkPowerUpCollisions();

        // Periodically switch each enemy's behavior strategy randomly every 30 seconds
        if (strategySwitchTimer >= 30f && !enemies.isEmpty()) {
            strategySwitchTimer = 0f;
            for (Enemy e : enemies) {
                EnemyBehaviorStrategy current = e.getBehaviorStrategy();
                EnemyBehaviorStrategy next = current;
                if (behaviorStrategies != null && behaviorStrategies.length > 0) {
                    if (behaviorStrategies.length == 1) {
                        next = behaviorStrategies[0];
                    } else {
                        // Ensure a different strategy is selected when possible
                        do {
                            next = behaviorStrategies[rng.nextInt(behaviorStrategies.length)];
                        } while (next == current);
                    }
                    e.setBehaviorStrategy(next);
                }
            }
            logger.debug("Switched enemy behaviors randomly for all enemies");
        }

        // Finalize reloads and send ammo updates if completed
        for (Player p : players) {
            if (p.getCurrentWeapon() != null) {
                p.getCurrentWeapon().update(deltaTime);
            }
        }

        GameWorldMessage m = MessageCreator.generateGWMMessage(enemies, bullets, players, spikes, placedSpikes, powerUpsArray);
        server.sendToAllUDP(m);

    }

    public float getGameTime() {
        return gameTime;
    }

    /**
     * Spawns power-ups and adds them to different collections.
     * Demonstrates Iterator pattern with 3 different data structures.
     */
    private void spawnRandomPowerUp() {
        if (powerUpSpawnTime >= 5.0f && powerUpsArray.size() < 5) { //spawn timer && amount
            powerUpSpawnTime = 0;
            
            PowerUp.PowerUpType[] types = PowerUp.PowerUpType.values();
            PowerUp.PowerUpType randomType = types[rng.nextInt(types.length)];
            
            PowerUp powerUp = new PowerUp(
                powerUpIdCounter++,
                rng.nextInt(1000),
                rng.nextInt(1000),
                randomType,
                8.0f // effect duration
            );
            
            powerUpsArray.add(powerUp);
            powerUpsList.add(powerUp);
            powerUpsMap.add(powerUp);
            
            logger.debug("Spawned " + randomType + " power-up. Total: " + powerUpsArray.size());
        }
    }

    /**
     * Check collisions using Iterator pattern.
     * Demonstrates iterating through different collection types uniformly.
     */
    private void checkPowerUpCollisions() {
        Iterator<PowerUp> iter = powerUpsArray.createIterator();
        List<PowerUp> toRemove = new ArrayList<>();
        
        for (iter.first(); !iter.isDone(); iter.next()) {
            PowerUp powerUp = iter.currentItem();
            if (powerUp == null || !powerUp.isVisible()) continue;
            
            for (Player player : players) {
                if (player.getBoundRect().overlaps(powerUp.getBoundRect())) {
                    applyPowerUpEffect(player, powerUp);
                    powerUp.setVisible(false);
                    toRemove.add(powerUp);
                    break;
                }
            }
        }
        
        for (PowerUp p : toRemove) {
            powerUpsArray.remove(p);
            powerUpsList.remove(p);
            powerUpsMap.remove(p);
        }
    }

    private void applyPowerUpEffect(Player player, PowerUp powerUp) {
        switch (powerUp.getType()) {
        case SPEED_BOOST:
            player.applySpeedBoost(gameTime, powerUp.getDuration());
            logger.debug("Player " + player.getId() + " collected SPEED_BOOST (x2.0 speed for " + 
                        powerUp.getDuration() + "s)");
            break;
            
        case DAMAGE_BOOST:
            player.applyDamageBoost(gameTime, powerUp.getDuration());
            logger.debug("Player " + player.getId() + " collected DAMAGE_BOOST (x1.5 damage for " + 
                        powerUp.getDuration() + "s)");
            break;
            
        case SHIELD:
            player.applyShield(gameTime, powerUp.getDuration());
            logger.debug("Player " + player.getId() + " collected SHIELD (50 HP shield for " + 
                        powerUp.getDuration() + "s)");
            break;
            
        case AMMO_REFILL:
            player.applyAmmoRefill(gameTime);
            logger.debug("Player " + player.getId() + " collected AMMO_REFILL (instant reload)");

            InventoryUpdateMessage reloadMsg = new InventoryUpdateMessage();
            reloadMsg.setPlayerId(player.getId());
            reloadMsg.setSpikeCount(player.getRefill());
            reloadMsg.setShouldReload(true);
            server.sendToAllUDP(reloadMsg); 
            break;
    }
    }



    /**
     * Spawns an enemy to the random location. In 0.4 second if enemy list size is
     * lessthan 15.
     */
    private void spawnRandomEnemy() {
        if (enemyTime >= 0.4 && enemies.stream().filter(Enemy::isVisible).count() <= 15) {
            enemyTime = 0;

            EnemyBehaviorStrategy strategy = behaviorStrategies[strategyIndex];
            strategyIndex = (strategyIndex + 1) % behaviorStrategies.length;

            boolean respawned = false;

            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
                if (!e.isVisible()) {
                    Enemy cloned = e.clone();
                    cloned.setPosition(new Vector2(new SecureRandom().nextInt(1000), new SecureRandom().nextInt(1000)));
                    cloned.setBehaviorStrategy(strategy);
                    cloned.setVisible(true);

                    enemies.set(i, cloned);
                    respawned = true;
                }
            }

            if (!respawned) {
                Enemy newEnemy = new Enemy(
                        new SecureRandom().nextInt(1000),
                        new SecureRandom().nextInt(1000),
                        10,
                        strategy);
                enemies.add(newEnemy);
            }
            logger.debug("Spawned enemy with " + strategy.getStrategyName() + " behavior");
        }
    }

    private void spawnRandomSpike() {
        if (spikeSpawnTime >= 5.0f && spikes.size() < 5) {
            spikeSpawnTime = 0;
            Spike spike = new Spike(new SecureRandom().nextInt(1000), new SecureRandom().nextInt(1000), 30);
            spikes.add(spike);
            logger.debug("Spawned spike pickup. Total spikes: " + spikes.size());
        }
    }

    private void checkCollision() {

        for (Bullet b : bullets) {

            for (Enemy e : enemies) {

                if (b.isVisible() && e.getBoundRect().overlaps(b.getBoundRect())) {
                    b.setVisible(false);
                    e.setVisible(false);
                    players.stream().filter(p -> p.getId() == b.getId()).findFirst().ifPresent(Player::increaseHealth);
                }

            }
            for (Player p : players) {
                if (b.isVisible() && p.getBoundRect().overlaps(b.getBoundRect()) && p.getId() != b.getId()) {
                    b.setVisible(false);

                    players.stream().filter(attacker -> attacker.getId() == b.getId()).findFirst()
                    .ifPresent(attacker -> {
                        if (attacker.getCurrentWeapon() != null) {
                            Weapon attackerWeapon = attacker.getCurrentWeapon();
                            
                            // apply damage boost
                            float baseDamage = attackerWeapon.getDamage();
                            float damageMultiplier = attacker.getDamageMultiplier();
                            int finalDamage = (int) (baseDamage * damageMultiplier);
                            
                            String weaponName = attackerWeapon.getName();
                            
                            System.out.println("Player " + p.getId() + " hit by " + weaponName + 
                                " for " + finalDamage + " damage (base: " + baseDamage + 
                                ", multiplier: " + damageMultiplier + ")");
                            
                            p.hit(finalDamage);  // shield take damage if available
                        }
                    });
                    if (!p.isAlive()) {

                        PlayerDiedMessage m = new PlayerDiedMessage();
                        m.setPlayerId(p.getId());
                        server.sendToAllUDP(m);
                    }

                }
            }

        }

        // Check spike pickup collisions
        for (Spike spike : spikes) {
            for (Player player : players) {
                if (spike.isVisible() && player.getBoundRect().overlaps(spike.getBoundRect())) {
                    spike.setVisible(false);
                    player.addSpike();

                    InventoryUpdateMessage inventoryMsg = new InventoryUpdateMessage();
                    inventoryMsg.setPlayerId(player.getId());
                    inventoryMsg.setSpikeCount(player.getSpikeCount());
                    server.sendToAllUDP(inventoryMsg);

                    logger.debug("Player " + player.getId() + " picked up spike. Total: " + player.getSpikeCount());
                }
            }
        }

        // Check placed spike collisions with other players
        for (PlacedSpike placedSpike : placedSpikes) {
            for (Player player : players) {
                // Don't damage the player who placed the spike
                if (placedSpike.isVisible() && !placedSpike.isConsumed() &&
                        player.getId() != placedSpike.getPlayerId() &&
                        player.getBoundRect().overlaps(placedSpike.getBoundRect())) {

                    placedSpike.setConsumed(true);
                    placedSpike.setVisible(false);

                    int spikeDamage = 20;
                    player.hit(spikeDamage);

                    logger.debug("Player " + player.getId() + " hit by spike for " + spikeDamage + " damage");

                    if (!player.isAlive()) {
                        PlayerDiedMessage m = new PlayerDiedMessage();
                        m.setPlayerId(player.getId());
                        server.sendToAllUDP(m);
                    }
                }
            }
        }

    }

    @Override
    public void loginReceived(Connection con, LoginMessage m) {

        int id = idPool.getUserID();
        players.add(new Player(m.getX(), m.getY(), 50, id));
        logger.debug("Login Message recieved from : " + id);

        giveWeaponToPlayer(id, "pistol");
        sendWeaponInfoToPlayer(id);
        logger.debug("Login Message recieved from : " + id + " with default weapon");

        m.setPlayerId(id);
        server.sendToUDP(con.getID(), m);
        // Track which player ID belongs to this connection for proper cleanup on
        // disconnect
        connectionToPlayerId.put(con.getID(), id);
    }

    @Override
    public void logoutReceived(LogoutMessage m) {

        removePlayerById(m.getPlayerId());
        logger.debug("Logout Message recieved from : " + m.getPlayerId() + " Size: " + players.size());

    }

    @Override
    public void disconnected(Connection con) {
        Integer playerId = connectionToPlayerId.remove(con.getID());
        if (playerId != null) {
            removePlayerById(playerId);
            logger.debug("Disconnected connection id: " + con.getID() + " mapped to player: " + playerId);
        } else {
            logger.debug("Disconnected connection id: " + con.getID() + " had no mapped player");
        }
    }

    private void removePlayerById(int playerId) {
        players.stream().filter(p -> p.getId() == playerId).findFirst().ifPresent(p -> {
            players.remove(p);
            idPool.putUserIDBack(p.getId());
        });
        // Clean up per-player auxiliary state
        playerWeapons.remove(playerId);
        playerSpikeCommands.remove(playerId);
        // Also drop reverse mapping if exists (in case called from logout)
        connectionToPlayerId.values().removeIf(id -> id == playerId);
    }

    @Override
    public void playerMovedReceived(PositionMessage m) {
        players.stream().filter(p -> p.getId() == m.getPlayerId()).findFirst().ifPresent(p -> {

            Vector2 v = p.getPosition();
            float baseSpeed = 200f;
            float actualSpeed = baseSpeed * p.getSpeedMultiplier(); //apply speed boost
            
            switch (m.getDirection()) {
                case LEFT:
                    v.x -= deltaTime * actualSpeed;
                    break;
                case RIGHT:
                    v.x += deltaTime * actualSpeed;
                    break;
                case UP:
                    v.y -= deltaTime * actualSpeed;
                    break;
                case DOWN:
                    v.y += deltaTime * actualSpeed;
                    break;
                default:
                    break;
            }

        });

    }

    @Override
    public void shootReceived(ShootMessage m) {
        players.stream().filter(p -> p.getId() == m.getPlayerId()).findFirst()
                .ifPresent(p -> {
                    if (p.getCurrentWeapon() != null) {
                        p.getCurrentWeapon().requestFire(this, p, m.getAngleDeg());
                    }
                });

    }

    private void sendAmmoUpdate(Player p) {
        if (p.getCurrentWeapon() == null)
            return;
        Weapon w = p.getCurrentWeapon();
        AmmoUpdateMessage msg = new AmmoUpdateMessage();
        msg.setPlayerId(p.getId());
        msg.setCurrentAmmo(w.getCurrentAmmo());
        msg.setAmmoCapacity(w.getAmmoCapacity());
        server.sendToAllUDP(msg);
    }

    /**
     * Helper for weapons to spawn bullets consistently.
     */
    public void createBullet(BulletType type, Player owner, float angleRad) {
        Bullet b = bulletFactory.createBullet(
                type,
                owner.getPosition().x + owner.getBoundRect().width / 2,
                owner.getPosition().y + owner.getBoundRect().height / 2,
                angleRad,
                owner.getId());
        bullets.add(b);
    }

    public void giveWeaponToPlayer(int playerId, String weaponConfig) {
        players.stream().filter(p -> p.getId() == playerId).findFirst()
                .ifPresent(p -> {
                    String baseConfig = extractBaseConfig(weaponConfig);
                    Weapon weapon = createWeaponByConfig(baseConfig);
                    weapon = decorateWeaponForConfig(weapon, weaponConfig);

                    weapon.addListener((w) -> {
                        sendAmmoUpdate(p);
                    });

                    weapon.setCurrentAmmo(weapon.getAmmoCapacity());
                    System.out.println("Created weapon: " + weapon.getName() + " with damage: " + weapon.getDamage());

                    p.equipWeapon(weapon);
                    playerWeapons.put(playerId, weapon);
                });
    }

    private Weapon createWeaponByConfig(String config) {
        return switch (config) {
            case "assault_rifle" -> weaponDirector.createAssaultRifle();
            case "combat_shotgun" -> weaponDirector.createCombatShotgun();
            case "precision_sniper" -> weaponDirector.createPrecisionSniper();
            default -> weaponDirector.createAssaultRifle();
        };
    }

    /**
     * Applies context-appropriate attachments to a base weapon using Decorator
     * chaining.
     * You can expand this mapping or parse richer configs (e.g.,
     * "assault_rifle+mag:15+scope:4x:150").
     */
    private Weapon decorateWeaponForConfig(Weapon base, String weaponConfig) {
        if (base == null)
            return null;
        if (weaponConfig == null || weaponConfig.isEmpty())
            return base;

        // Support either simple presets or +attachment tokens
        if (!weaponConfig.contains("+")) {
            switch (weaponConfig) {
                case "precision_sniper":
                    base = new ScopeAttachment(base, "8x Scope", 200f);
                    return base;
                case "assault_rifle":
                    base = new ExtendedMagazineAttachment(base, 15);
                    base = new GripAttachment(base, "Tactical Grip", 0.5f);
                    return base;
                case "combat_shotgun":
                    base = new DamageBoostAttachment(base, 5f);
                    return base;
                default:
                    if (weaponConfig.contains("silenced")) {
                        base = new SilencerAttachment(base, "Silenced Barrel", 2f);
                    }
                    return base;
            }
        }

        // Advanced format: base+att1+att2+...
        String[] parts = weaponConfig.split("\\+");
        // parts[0] is base, already used
        for (int i = 1; i < parts.length; i++) {
            String token = parts[i].trim();
            if (token.isEmpty())
                continue;
            try {
                // scope:name:bonusRange
                if (token.startsWith("scope")) {
                    String[] t = token.split(":");
                    String name = t.length > 1 ? t[1] : "Scope";
                    float bonus = t.length > 2 ? Float.parseFloat(t[2]) : 150f;
                    base = new ScopeAttachment(base, name, bonus);
                    continue;
                }
                // mag:extraAmmo
                if (token.startsWith("mag")) {
                    String[] t = token.split(":");
                    int extra = t.length > 1 ? Integer.parseInt(t[1]) : 15;
                    base = new ExtendedMagazineAttachment(base, extra);
                    continue;
                }
                // grip:name:bonusFire
                if (token.startsWith("grip")) {
                    String[] t = token.split(":");
                    String name = t.length > 1 ? t[1] : "Grip";
                    float bonus = t.length > 2 ? Float.parseFloat(t[2]) : 0.5f;
                    base = new GripAttachment(base, name, bonus);
                    continue;
                }
                // dmg:bonusDamage
                if (token.startsWith("dmg")) {
                    String[] t = token.split(":");
                    float bonus = t.length > 1 ? Float.parseFloat(t[1]) : 5f;
                    base = new DamageBoostAttachment(base, bonus);
                    continue;
                }
                // silencer:name:penalty OR "silenced"
                if (token.startsWith("silencer") || token.equals("silenced")) {
                    if (token.equals("silenced")) {
                        base = new SilencerAttachment(base, "Silenced Barrel", 2f);
                    } else {
                        String[] t = token.split(":");
                        String name = t.length > 1 ? t[1] : "Silenced Barrel";
                        float penalty = t.length > 2 ? Float.parseFloat(t[2]) : 2f;
                        base = new SilencerAttachment(base, name, penalty);
                    }
                    continue;
                }
            } catch (Exception ex) {
                logger.warn("Failed to parse attachment token: " + token + ", err=" + ex.getMessage());
            }
        }
        return base;
    }

    private String extractBaseConfig(String fullConfig) {
        if (fullConfig == null || fullConfig.isEmpty())
            return "assault_rifle";
        int plus = fullConfig.indexOf('+');
        if (plus < 0)
            return fullConfig;
        return fullConfig.substring(0, plus);
    }

    private void sendWeaponInfoToPlayer(int playerId) {
        players.stream().filter(p -> p.getId() == playerId).findFirst()
                .ifPresent(p -> {
                    if (p.getCurrentWeapon() != null) {
                        Weapon weapon = p.getCurrentWeapon();

                        WeaponInfoMessage info = new WeaponInfoMessage();
                        info.setPlayerId(playerId);
                        info.setWeaponName(weapon.getName());

                        StringBuilder components = new StringBuilder();
                        if (weapon.getBarrel() != null)
                            components.append(weapon.getBarrel()).append(", ");
                        if (weapon.getScope() != null && !weapon.getScope().equals("null"))
                            components.append(weapon.getScope()).append(", ");
                        if (weapon.getStock() != null)
                            components.append(weapon.getStock()).append(", ");
                        if (weapon.getMagazine() != null && !weapon.getMagazine().equals("null"))
                            components.append(weapon.getMagazine()).append(", ");
                        if (weapon.getGrip() != null && !weapon.getGrip().equals("null"))
                            components.append(weapon.getGrip());

                        String componentsStr = components.toString().replaceAll(", $", "");
                        info.setComponents(componentsStr);

                        String stats = String.format("Dmg:%.0f | Range:%.0f | Fire:%.1f | Ammo:%d",
                                weapon.getDamage(), weapon.getRange(), weapon.getFireRate(), weapon.getAmmoCapacity());
                        info.setStats(stats);

                        server.sendToAllUDP(info);
                    }
                });
    }

    @Override
    public void weaponChangeReceived(WeaponChangeMessage m) {
        giveWeaponToPlayer(m.getPlayerId(), m.getWeaponConfig());

        sendWeaponInfoToPlayer(m.getPlayerId());

        logger.debug("Player " + m.getPlayerId() + " changed weapon to: " + m.getWeaponConfig());
    }

    @Override
    public void placeSpikeReceived(PlaceSpikeMessage m) {
        players.stream().filter(p -> p.getId() == m.getPlayerId()).findFirst()
                .ifPresent(player -> {
                    if (player.hasSpikes()) {
                        // Calculate position in front of player based on rotation
                        float distance = 60; // Place spike 60 units in front
                        float angleRad = (float) Math.toRadians(m.getRotation());
                        float x = player.getPosition().x + (float) Math.cos(angleRad) * distance;
                        float y = player.getPosition().y - (float) Math.sin(angleRad) * distance;

                        PlaceSpikeCommand command = new PlaceSpikeCommand(player, placedSpikes, x, y, m.getRotation());
                        command.execute();

                        playerSpikeCommands.computeIfAbsent(player.getId(), k -> new Stack<>()).push(command);

                        InventoryUpdateMessage inventoryMsg = new InventoryUpdateMessage();
                        inventoryMsg.setPlayerId(player.getId());
                        inventoryMsg.setSpikeCount(player.getSpikeCount());
                        server.sendToAllUDP(inventoryMsg);

                        logger.debug("Player " + player.getId() + " placed spike at (" + x + ", " + y + ") rotation: "
                                + m.getRotation());
                    }
                });
    }

    @Override
    public void reloadReceived(ReloadMessage m) {
        players.stream().filter(p -> p.getId() == m.getPlayerId()).findFirst()
                .ifPresent(p -> {
                    if (p.getCurrentWeapon() != null) {
                        p.getCurrentWeapon().requestReload();
                    }
                });
    }

    @Override
    public void undoSpikeReceived(UndoSpikeMessage m) {
        Stack<Command> commandStack = playerSpikeCommands.get(m.getPlayerId());

        while (commandStack != null && !commandStack.isEmpty()) {
            Command lastCommand = commandStack.pop();

            if (lastCommand.canUndo()) {
                lastCommand.undo();

                players.stream().filter(p -> p.getId() == m.getPlayerId()).findFirst()
                        .ifPresent(player -> {
                            InventoryUpdateMessage inventoryMsg = new InventoryUpdateMessage();
                            inventoryMsg.setPlayerId(player.getId());
                            inventoryMsg.setSpikeCount(player.getSpikeCount());
                            server.sendToAllUDP(inventoryMsg);
                        });

                logger.debug("Player " + m.getPlayerId() + " undid spike placement");
                return;
            } else {
                logger.debug("Player " + m.getPlayerId() + " spike was consumed, skipping to previous spike");
            }
        }

        if (commandStack == null || commandStack.isEmpty()) {
            logger.debug("Player " + m.getPlayerId() + " has no spikes to undo");
        }
    }

}
