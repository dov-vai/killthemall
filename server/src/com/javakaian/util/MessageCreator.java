package com.javakaian.util;

import com.javakaian.network.messages.GameWorldMessage;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.shooter.shapes.Player;

import java.util.List;

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
    public static GameWorldMessage generateGWMMessage(List<Enemy> enemies, List<Bullet> bullets, List<Player> players) {

        GameWorldMessage gwm = new GameWorldMessage();
        float[] coordinates = new float[enemies.size() * 2];

        for (int i = 0; i < enemies.size(); i++) {
            var position = enemies.get(i).getPosition();

            coordinates[i * 2] = position.x;
            coordinates[i * 2 + 1] = position.y;
        }

        gwm.setEnemies(coordinates);

        float[] pcord = new float[players.size() * 4];
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);
            var position = player.getPosition();

            pcord[i * 4] = position.x;
            pcord[i * 4 + 1] = position.y;
            pcord[i * 4 + 2] = player.getId();
            pcord[i * 4 + 3] = player.getHealth();
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

        return gwm;
    }

}
