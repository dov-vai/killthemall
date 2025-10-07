package com.javakaian.shooter;

import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.shooter.shapes.GameObject;

import java.security.SecureRandom;

public class GameEntityFactory {
    private static final SecureRandom random = new SecureRandom();

    //Factory for creating entities
    public static GameObject createEnemy(){
        int x = random.nextInt(1000);
        int y = random.nextInt(1000);
        return new Enemy(x, y, 10);
    }

    public static GameObject createPlayer(float x, float y, int id){
        return new Player(x, y, 50, id);
    }

    public static GameObject createBullet(float x, float y, float angle, int playerId){
        return new Bullet(x, y, 10, angle, playerId);
    }
}
