package com.javakaian.util;

import com.javakaian.network.messages.GameWorldMessage;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.shapes.Spike;
import com.javakaian.shooter.shapes.PlacedSpike;
import com.javakaian.shooter.shapes.PowerUp;
import com.javakaian.shooter.iterator.Iterator;
import com.javakaian.shooter.iterator.PowerUpCollection; 

import java.util.List;
import java.util.ArrayList;

public class MessageCreator {

    private MessageCreator() {

    }

    /**
     * Creates a GameWorldMessage. This message will be broadcasted to the all
     * clients over UDP.
     * <p>
     * Every objects in server like Enemies,Players,Bullets will be converted to the
     * float arrays and broadcasted.
     */
    public static GameWorldMessage generateGWMMessage(List<Enemy> enemies, List<Bullet> bullets, List<Player> players, List<Spike> spikes, List<PlacedSpike> placedSpikes, PowerUpCollection powerUps) {

        GameWorldMessage gwm = new GameWorldMessage();
        float[] coordinates = new float[enemies.size() * 2];

        for (int i = 0; i < enemies.size(); i++) {
            var position = enemies.get(i).getPosition();

            coordinates[i * 2] = position.x;
            coordinates[i * 2 + 1] = position.y;
        }

        gwm.setEnemies(coordinates);

        float[] pcord = new float[players.size() * 6];
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);
            var position = player.getPosition();

            pcord[i * 6] = position.x;
            pcord[i * 6 + 1] = position.y;
            pcord[i * 6 + 2] = player.getId();
            pcord[i * 6 + 3] = player.getHealth();
            pcord[i * 6 + 4] = player.hasShield() ? 1f : 0f;
            pcord[i * 6 + 5] = player.getShieldHealth();
        }

        gwm.setPlayers(pcord);

        float[] barray = new float[bullets.size() * 3];
        for (int i = 0; i < bullets.size(); i++) {
            var bullet = bullets.get(i);
            var position = bullet.getPosition();

            barray[i * 3] = position.x;
            barray[i * 3 + 1] = position.y;
            barray[i * 3 + 2] = bullet.getSize();
        }
        gwm.setBullets(barray);

        float[] spikeArray = new float[spikes.size() * 3];
        for (int i = 0; i < spikes.size(); i++) {
            var spike = spikes.get(i);
            var position = spike.getPosition();

            spikeArray[i * 3] = position.x;
            spikeArray[i * 3 + 1] = position.y;
            spikeArray[i * 3 + 2] = spike.getSize();
        }
        gwm.setSpikes(spikeArray);

        float[] placedSpikeArray = new float[placedSpikes.size() * 4];
        for (int i = 0; i < placedSpikes.size(); i++) {
            var placedSpike = placedSpikes.get(i);
            var position = placedSpike.getPosition();

            placedSpikeArray[i * 4] = position.x;
            placedSpikeArray[i * 4 + 1] = position.y;
            placedSpikeArray[i * 4 + 2] = placedSpike.getSize();
            placedSpikeArray[i * 4 + 3] = placedSpike.getRotation();
        }
        gwm.setPlacedSpikes(placedSpikeArray);

        List<PowerUp> powerUpList = new ArrayList<>();
        Iterator<PowerUp> iter = powerUps.createIterator();
        for (iter.first(); !iter.isDone(); iter.next()) {
            PowerUp p = iter.currentItem();
            if (p != null && p.isVisible()) {
                powerUpList.add(p);
            }
        }
        
        float[] powerUpArray = new float[powerUpList.size() * 4];
        for (int i = 0; i < powerUpList.size(); i++) {
            var powerUp = powerUpList.get(i);
            var position = powerUp.getPosition();
            
            powerUpArray[i * 4] = position.x;
            powerUpArray[i * 4 + 1] = position.y;
            powerUpArray[i * 4 + 2] = powerUp.getSize();
            powerUpArray[i * 4 + 3] = powerUp.getType().ordinal(); // Type as number
        }
        gwm.setPowerUps(powerUpArray);

        return gwm;
    }

}
