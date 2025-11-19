package com.javakaian.network.messages;

/**
 * This message will be broadcast to all clients 60 times per second. It
 * contains position arrays of enemies,players and bullets.
 *
 * @author oguz
 */
public class GameWorldMessage {

    /**
     * X,Y Coordinates of all the enemies.-> x1,y1,x2,y2,x3,y3..
     */
    private float[] enemies;
    /**
     * X,Y,ID,Health,HasShield,ShieldHealth of all the players.->
     * x1,y1,ID1,Health1,Shield1,ShieldHP1,x2,y2,ID2,Health2,Shield2,ShieldHP2...
     */
    private float[] players;
    /**
     * X,Y,Size of all the bullets.-> x1,y1,s1,x2,y2,s2,x3,y3,s3..
     */
    private float[] bullets;

    /**
     * X,Y,Size of all the spike pickups.-> x1,y1,s1,x2,y2,s2..
     */
    private float[] spikes;

    /**
     * X,Y,Size,Rotation of all placed spikes.-> x1,y1,s1,r1,x2,y2,s2,r2..
     */
    private float[] placedSpikes;

    /**
     * X,Y,Size,Rotation of all placed powerUps.-> x1,y1,s1,r1,x2,y2,s2,r2..
     */
    private float[] powerUps;

    public float[] getEnemies() {
        return enemies;
    }

    public void setEnemies(float[] enemies) {
        this.enemies = enemies;
    }

    public float[] getPlayers() {
        return players;
    }

    public void setPlayers(float[] players) {
        this.players = players;
    }

    public float[] getBullets() {
        return bullets;
    }

    public void setBullets(float[] bullets) {
        this.bullets = bullets;
    }

    public float[] getSpikes() {
        return spikes;
    }

    public void setSpikes(float[] spikes) {
        this.spikes = spikes;
    }

    public float[] getPlacedSpikes() {
        return placedSpikes;
    }

    public void setPlacedSpikes(float[] placedSpikes) {
        this.placedSpikes = placedSpikes;
    }

    public float[] getPowerUps() {
        return powerUps;
    }
    
    public void setPowerUps(float[] powerUps) {
        this.powerUps = powerUps;
    }

}
