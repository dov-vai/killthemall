package com.javakaian.shooter.shapes;

import com.javakaian.network.OServer;
import com.javakaian.shooter.ServerWorld;
import com.javakaian.network.messages.*;

import java.util.ArrayList;
import java.util.List;

public class CollisionVisitor implements GameObjectVisitor {

    private final GameObjectComposite worldObjects;
    private OServer server;

    public CollisionVisitor(GameObjectComposite worldObjects, OServer server) {
        this.worldObjects = worldObjects;
        this.server = server;
    }

    @Override
    public void visit(Player player) {

        // BULLET -> PLAYER
        List<Bullet> bullets = new ArrayList<>(worldObjects.getAll(Bullet.class));
        List<Player> players = worldObjects.getAll(Player.class);

        for (Bullet b : bullets) {
            if (!b.isVisible() || b.getId() == player.getId()) continue;

            if (player.getBoundRect().overlaps(b.getBoundRect())) {
                b.setVisible(false);

                players.stream()
                        .filter(attacker -> attacker.getId() == b.getId())
                        .findFirst()
                        .ifPresent(attacker -> {
                            if (attacker.getCurrentWeapon() != null) {
                                float baseDamage = attacker.getCurrentWeapon().getDamage();
                                float multiplier = attacker.getDamageMultiplier();
                                player.hit((int) (baseDamage * multiplier));
                            }
                        });

                break;
            }
        }

        // SPIKE PICKUP
        List<Spike> spikes = new ArrayList<>(worldObjects.getAll(Spike.class));
        for (Spike spike : spikes) {
            if (spike.getBoundRect().overlaps(player.getBoundRect())) {
                // increment spike count
                player.addSpike();

                // remove spike from world
                worldObjects.remove(spike);

                // send inventory update to all clients
                InventoryUpdateMessage inventoryMsg = new InventoryUpdateMessage();
                inventoryMsg.setPlayerId(player.getId());
                inventoryMsg.setSpikeCount(player.getSpikeCount());
                server.sendToAllUDP(inventoryMsg);

                System.out.println("Player " + player.getId() + " picked up spike. Total: " + player.getSpikeCount());
            }
        }

        // PLACED SPIKE DAMAGE
        List<PlacedSpike> placedSpikes = new ArrayList<>(worldObjects.getAll(PlacedSpike.class));
        for (PlacedSpike spike : placedSpikes) {
            if (!spike.isVisible() || spike.isConsumed()) continue;

            if (player.getId() != spike.getPlayerId()
                    && player.getBoundRect().overlaps(spike.getBoundRect())) {

                player.hit(20);
                spike.setConsumed(true);
                worldObjects.remove(spike);
                break;
            }
        }
    }

    @Override
    public void visit(Enemy enemy) {

        List<Bullet> bullets = new ArrayList<>(worldObjects.getAll(Bullet.class));

        for (Bullet b : bullets) {
            if (!b.isVisible()) continue;

            if (b.getBoundRect().overlaps(enemy.getBoundRect())) {
                worldObjects.remove(b);
                worldObjects.remove(enemy);
                break;
            }
        }
    }

    @Override public void visit(Bullet bullet) {}
    @Override public void visit(Spike spike) {}
    @Override public void visit(PlacedSpike spike) {}
}
