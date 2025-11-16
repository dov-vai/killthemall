package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.weapons.Weapon;

public class Player {

    private float size;
    private Vector2 position;
    private int id;
    private Rectangle boundRect;
    private boolean alive;
    private int health;

    //weapon system
    private Weapon currentWeapon;
    private float lastShotTime = 0;

    //spike inventory
    private int spikeCount;

    public Player(float x, float y, float size, int id) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.id = id;
        this.boundRect = new Rectangle(x, y, this.size, this.size);
        this.alive = true;
        this.health = 100;
        this.spikeCount = 0;
    }

    public void update(float deltaTime) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rectangle getBoundRect() {
        return boundRect;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getHealth() {
        return this.health;
    }

    public void increaseHealth() {
        if (this.health >= 100)
            return;
        this.health += 10;
        if (this.health > 100) this.health = 100;
    }

    public void hit(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.alive = false;
        }
    }

    public boolean isVisible() {
        return alive;
    }

    public void setVisible(boolean visible) {
        this.alive = visible;
    }

    //weapons system
    public void equipWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
        this.lastShotTime = 0;
        System.out.println("Player " + id + " equipped: " + weapon.getDescription());
    }

    public boolean canShoot(float currentTime) {
        if (currentWeapon == null) return false;

        float timeSinceLastShot = currentTime - lastShotTime;
        float requiredCooldown = 1.0f / currentWeapon.getFireRate();

        boolean canShoot = timeSinceLastShot >= requiredCooldown;

        System.out.println("Player " + id + " canShoot check: time=" + currentTime +
                " lastShot=" + lastShotTime + " timeSince=" + timeSinceLastShot +
                " required=" + requiredCooldown + " result=" + canShoot);

        return canShoot;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void recordShot(float currentTime) {
        this.lastShotTime = currentTime;
        System.out.println("Player " + id + " shot recorded at time " + currentTime);
    }

    //spike inventory system
    public int getSpikeCount() {
        return spikeCount;
    }

    public void addSpike() {
        this.spikeCount++;
    }

    public boolean hasSpikes() {
        return spikeCount > 0;
    }

    public void removeSpike() {
        if (spikeCount > 0) {
            spikeCount--;
        }
    }

}
