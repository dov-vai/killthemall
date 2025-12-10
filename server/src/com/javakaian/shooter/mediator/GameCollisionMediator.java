package com.javakaian.shooter.mediator;

import com.javakaian.network.OServer;
import com.javakaian.network.messages.InventoryUpdateMessage;
import com.javakaian.network.messages.PlayerDiedMessage;
import com.javakaian.shooter.shapes.*;
import com.javakaian.shooter.iterator.PowerUpCollection;
import com.javakaian.shooter.iterator.Iterator;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;

/**
 * Concrete implementation of the CollisionMediator.
 * Handles all collision detection logic between game entities.
 */
public class GameCollisionMediator implements CollisionMediator {
    
    private List<Player> players;
    private List<Enemy> enemies;
    private List<Spike> spikes;
    private List<PlacedSpike> placedSpikes;
    private PowerUpCollection powerUpsArray;
    private PowerUpCollection powerUpsList;
    private PowerUpCollection powerUpsMap;
    
    private OServer server;
    private float gameTime;
    
    private Logger logger = Logger.getLogger(GameCollisionMediator.class);
    
    public GameCollisionMediator(
            List<Player> players,
            List<Enemy> enemies,
            List<Bullet> bullets,
            List<Spike> spikes,
            List<PlacedSpike> placedSpikes,
            PowerUpCollection powerUpsArray,
            PowerUpCollection powerUpsList,
            PowerUpCollection powerUpsMap,
            OServer server) {
        
        this.players = players;
        this.enemies = enemies;
        this.spikes = spikes;
        this.placedSpikes = placedSpikes;
        this.powerUpsArray = powerUpsArray;
        this.powerUpsList = powerUpsList;
        this.powerUpsMap = powerUpsMap;
        this.server = server;
    }
    
    public void setGameTime(float gameTime) {
        this.gameTime = gameTime;
    }
    
    @Override
    public void notify(Object sender, CollisionEvent event) {
        // Handle collisions based on what type of entity moved
        if (sender instanceof Bullet) {
            handleBulletCollisions((Bullet) sender);
        } else if (sender instanceof Player) {
            handlePlayerCollisions((Player) sender);
        } else if (sender instanceof PowerUp) {
            handlePowerUpCollisions((PowerUp) sender);
        } else if (sender instanceof Spike) {
            handleSpikePickupCollisions((Spike) sender);
        } else if (sender instanceof PlacedSpike) {
            handlePlacedSpikeCollisions((PlacedSpike) sender);
        }
    }
    
    /**
     * Handle collisions for a specific bullet that just moved
     */
    private void handleBulletCollisions(Bullet bullet) {
        if (!bullet.isVisible()) return;
        
        // Check bullet vs enemies
        for (Enemy e : enemies) {
            if (e.isVisible() && e.getBoundRect().overlaps(bullet.getBoundRect())) {
                bullet.setVisible(false);
                e.setVisible(false);
                
                // Increase health of player who shot the bullet
                players.stream()
                    .filter(p -> p.getId() == bullet.getId())
                    .findFirst()
                    .ifPresent(Player::increaseHealth);
                
                return; // Bullet hit something, stop checking
            }
        }
        
        // Check bullet vs players (not the shooter)
        for (Player p : players) {
            if (p.getId() == bullet.getId()) continue; // Skip shooter
            
            if (p.getBoundRect().overlaps(bullet.getBoundRect())) {
                bullet.setVisible(false);
                
                // Apply damage from attacker's weapon with damage boost
                players.stream()
                    .filter(attacker -> attacker.getId() == bullet.getId())
                    .findFirst()
                    .ifPresent(attacker -> {
                        if (attacker.getCurrentWeapon() != null) {
                            float baseDamage = attacker.getCurrentWeapon().getDamage();
                            float damageMultiplier = attacker.getDamageMultiplier();
                            int finalDamage = (int) (baseDamage * damageMultiplier);
                            
                            String weaponName = attacker.getCurrentWeapon().getName();
                            
                            System.out.println("Player " + p.getId() + " hit by " + weaponName + 
                                " for " + finalDamage + " damage (base: " + baseDamage + 
                                ", multiplier: " + damageMultiplier + ")");
                            
                            p.hit(finalDamage);
                        }
                    });
                
                // Check if player died
                if (!p.isAlive()) {
                    PlayerDiedMessage m = new PlayerDiedMessage();
                    m.setPlayerId(p.getId());
                    server.sendToAllUDP(m);
                }
                
                return; // Bullet hit something, stop checking
            }
        }
    }
    
    /**
     * Handle collisions for a specific player that just moved
     */
    private void handlePlayerCollisions(Player player) {
        // Check player vs spike pickups
        for (Spike spike : spikes) {
            if (spike.isVisible() && player.getBoundRect().overlaps(spike.getBoundRect())) {
                spike.setVisible(false);
                player.addSpike();
                
                // Send inventory update
                InventoryUpdateMessage inventoryMsg = new InventoryUpdateMessage();
                inventoryMsg.setPlayerId(player.getId());
                inventoryMsg.setSpikeCount(player.getSpikeCount());
                server.sendToAllUDP(inventoryMsg);
                
                logger.debug("Player " + player.getId() + " picked up spike. Total: " + 
                    player.getSpikeCount());
            }
        }
        
        // Check player vs placed spikes (not their own)
        for (PlacedSpike placedSpike : placedSpikes) {
            if (!placedSpike.isVisible() || placedSpike.isConsumed()) continue;
            if (player.getId() == placedSpike.getPlayerId()) continue; // Skip own spike
            
            if (player.getBoundRect().overlaps(placedSpike.getBoundRect())) {
                placedSpike.setConsumed(true);
                placedSpike.setVisible(false);
                
                int spikeDamage = 20;
                player.hit(spikeDamage);
                
                logger.debug("Player " + player.getId() + " hit by spike for " + 
                    spikeDamage + " damage");
                
                // Check if player died
                if (!player.isAlive()) {
                    PlayerDiedMessage m = new PlayerDiedMessage();
                    m.setPlayerId(player.getId());
                    server.sendToAllUDP(m);
                }
            }
        }
        
        // Check player vs power-ups
        Iterator<PowerUp> iter = powerUpsList.createIterator();
        List<PowerUp> toRemove = new ArrayList<>();
        
        for (iter.first(); !iter.isDone(); iter.next()) {
            PowerUp powerUp = iter.currentItem();
                        
            if (player.getBoundRect().overlaps(powerUp.getBoundRect())) {
                applyPowerUpEffect(player, powerUp);
                powerUp.setVisible(false);
                toRemove.add(powerUp);
            }
        }
        
        // Remove collected power-ups from all collections
        for (PowerUp p : toRemove) {
            powerUpsArray.remove(p);
            powerUpsList.remove(p);
            powerUpsMap.remove(p);
        }
    }
    
    /**
     * Handle collisions for a specific power-up (when player moves near it)
     */
    private void handlePowerUpCollisions(PowerUp powerUp) {
        if (!powerUp.isVisible()) return;
        
        for (Player player : players) {
            if (player.getBoundRect().overlaps(powerUp.getBoundRect())) {
                applyPowerUpEffect(player, powerUp);
                powerUp.setVisible(false);
                
                // Remove from all collections
                powerUpsArray.remove(powerUp);
                powerUpsList.remove(powerUp);
                powerUpsMap.remove(powerUp);
                
                return; // Power-up collected, stop checking
            }
        }
    }
    
    /**
     * Handle collisions for a specific spike pickup
     */
    private void handleSpikePickupCollisions(Spike spike) {
        if (!spike.isVisible()) return;
        
        for (Player player : players) {
            if (player.getBoundRect().overlaps(spike.getBoundRect())) {
                spike.setVisible(false);
                player.addSpike();
                
                // Send inventory update
                InventoryUpdateMessage inventoryMsg = new InventoryUpdateMessage();
                inventoryMsg.setPlayerId(player.getId());
                inventoryMsg.setSpikeCount(player.getSpikeCount());
                server.sendToAllUDP(inventoryMsg);
                
                logger.debug("Player " + player.getId() + " picked up spike. Total: " + 
                    player.getSpikeCount());
                
                return; // Spike picked up, stop checking
            }
        }
    }
    
    /**
     * Handle collisions for a specific placed spike
     */
    private void handlePlacedSpikeCollisions(PlacedSpike placedSpike) {
        if (!placedSpike.isVisible() || placedSpike.isConsumed()) return;
        
        for (Player player : players) {
            // Don't damage the player who placed the spike
            if (player.getId() == placedSpike.getPlayerId()) continue;
            
            if (player.getBoundRect().overlaps(placedSpike.getBoundRect())) {
                placedSpike.setConsumed(true);
                placedSpike.setVisible(false);
                
                int spikeDamage = 20;
                player.hit(spikeDamage);
                
                logger.debug("Player " + player.getId() + " hit by spike for " + 
                    spikeDamage + " damage");
                
                // Check if player died
                if (!player.isAlive()) {
                    PlayerDiedMessage m = new PlayerDiedMessage();
                    m.setPlayerId(player.getId());
                    server.sendToAllUDP(m);
                }
                
                return; // Spike hit someone, stop checking
            }
        }
    }
    
    /**
     * Apply power-up effect to player
     */
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
}
