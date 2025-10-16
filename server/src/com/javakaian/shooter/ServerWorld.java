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
import com.javakaian.shooter.strategy.*;
import com.javakaian.util.MessageCreator;
import org.apache.log4j.Logger;

import com.javakaian.shooter.weapons.Weapon;
import com.javakaian.shooter.weapons.Rifle;
import com.javakaian.shooter.weapons.Shotgun;
import com.javakaian.shooter.weapons.Sniper;
import com.javakaian.shooter.builder.WeaponDirector;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class ServerWorld implements OMessageListener {

    private List<Player> players;
    private List<Enemy> enemies;
    private List<Bullet> bullets;

    private OServer server;

    private float deltaTime = 0;

    private float enemyTime = 0f;

    private UserIdPool idPool;

    private Logger logger = Logger.getLogger(ServerWorld.class);

    private BulletFactory bulletFactory;

    //weapon system
    private WeaponDirector weaponDirector;
    private Map<Integer, Weapon> playerWeapons;
    
    private EnemyBehaviorStrategy[] behaviorStrategies;
    private int strategyIndex = 0;

    public ServerWorld() {

        server = new OServer(this);
        players = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        idPool = new UserIdPool();

        bulletFactory = new ConcreteBulletFactory();

        //weapon system
        weaponDirector = new WeaponDirector();
        playerWeapons = new HashMap<>();
        
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

        server.parseMessage();

        // update every object
        players.forEach(p -> p.update(deltaTime));
        enemies.forEach(e -> e.update(deltaTime, players)); // Pass players for AI behavior
        bullets.forEach(b -> b.update(deltaTime));

        checkCollision();

        // update object list. Remove necessary
        players.removeIf(p -> !p.isAlive());
        enemies.removeIf(e -> !e.isVisible());
        bullets.removeIf(b -> !b.isVisible());

        spawnRandomEnemy();

        GameWorldMessage m = MessageCreator.generateGWMMessage(enemies, bullets, players);
        server.sendToAllUDP(m);

    }

    /**
     * Spawns an enemy to the random location. In 0.4 second if enemy list size is
     * lessthan 15.
     */
    private void spawnRandomEnemy() {
        if (enemyTime >= 0.4 && enemies.size() <= 15) {
            enemyTime = 0;
            if (enemies.size() % 5 == 0)
                logger.debug("Number of enemies : " + enemies.size());
            
            EnemyBehaviorStrategy strategy = behaviorStrategies[strategyIndex];
            strategyIndex = (strategyIndex + 1) % behaviorStrategies.length;
            
            Enemy e = new Enemy(new SecureRandom().nextInt(1000), new SecureRandom().nextInt(1000), 10, strategy);
            enemies.add(e);
            
            logger.debug("Spawned enemy with " + strategy.getStrategyName() + " behavior");
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
                            int weaponDamage = (int) attackerWeapon.getDamage();
                            String weaponName = attackerWeapon.getName();
                            
                            System.out.println("Player " + p.getId() + " hit by " + weaponName + 
                                " for " + weaponDamage + " damage");
                            
                            p.hit(weaponDamage);
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
    }

    @Override
    public void logoutReceived(LogoutMessage m) {

        players.stream().filter(p -> p.getId() == m.getPlayerId()).findFirst().ifPresent(p -> {
            players.remove(p);
            idPool.putUserIDBack(p.getId());
        });
        logger.debug("Logout Message recieved from : " + m.getPlayerId() + " Size: " + players.size());

    }

    @Override
    public void playerMovedReceived(PositionMessage m) {

        players.stream().filter(p -> p.getId() == m.getPlayerId()).findFirst().ifPresent(p -> {

            Vector2 v = p.getPosition();
            switch (m.getDirection()) {
                case LEFT:
                    v.x -= deltaTime * 200;
                    break;
                case RIGHT:
                    v.x += deltaTime * 200;
                    break;
                case UP:
                    v.y -= deltaTime * 200;
                    break;
                case DOWN:
                    v.y += deltaTime * 200;
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
            // default
            BulletType bulletType = BulletType.STANDARD;

            if (p.getCurrentWeapon() != null) {
                Weapon weapon = p.getCurrentWeapon();
                bulletType = getBulletTypeFromWeapon(weapon);
                p.recordShot();
            } else {
                System.out.println("Player " + p.getId() + " fired default weapon -> " + bulletType + " bullet");
            }
            
            Bullet b = bulletFactory.createBullet(
                bulletType,
                p.getPosition().x + p.getBoundRect().width / 2,
                p.getPosition().y + p.getBoundRect().height / 2,
                m.getAngleDeg(),
                m.getPlayerId()
            );
            
            bullets.add(b);
        });

    }
    
    // weapon system
    private BulletType getBulletTypeFromWeapon(Weapon weapon) {
        if (weapon instanceof Rifle) return BulletType.STANDARD;
        if (weapon instanceof Shotgun) return BulletType.HEAVY;
        if (weapon instanceof Sniper) return BulletType.FAST;
        return BulletType.STANDARD;
    }
    
    public void giveWeaponToPlayer(int playerId, String weaponConfig) {
        players.stream().filter(p -> p.getId() == playerId).findFirst()
        .ifPresent(p -> {
            Weapon weapon = createWeaponByConfig(weaponConfig);
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

    private void sendWeaponInfoToPlayer(int playerId) {
        players.stream().filter(p -> p.getId() == playerId).findFirst()
        .ifPresent(p -> {
            if (p.getCurrentWeapon() != null) {
                Weapon weapon = p.getCurrentWeapon();
                
                WeaponInfoMessage info = new WeaponInfoMessage();
                info.setPlayerId(playerId);
                info.setWeaponName(weapon.getName());
                
                StringBuilder components = new StringBuilder();
                if (weapon.getBarrel() != null) components.append(weapon.getBarrel()).append(", ");
                if (weapon.getScope() != null && !weapon.getScope().equals("null")) components.append(weapon.getScope()).append(", ");
                if (weapon.getStock() != null) components.append(weapon.getStock()).append(", ");
                if (weapon.getMagazine() != null && !weapon.getMagazine().equals("null")) components.append(weapon.getMagazine()).append(", ");
                if (weapon.getGrip() != null && !weapon.getGrip().equals("null")) components.append(weapon.getGrip());
                
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

}
