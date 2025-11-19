package com.javakaian.shooter.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.javakaian.shooter.weapons.Weapon;
import com.javakaian.shooter.weapons.bridge.BridgeWeapon;

public class Player{

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

    //iterator - powerups
    private boolean hasSpeedBoost = false;
    private float speedBoostEndTime = 0f;
    private float speedMultiplier = 1.0f;
    private static final float SPEED_BOOST_MULTIPLIER = 2.0f;
    
    private boolean hasDamageBoost = false;
    private float damageBoostEndTime = 0f;
    private float damageMultiplier = 1.0f;
    private static final float DAMAGE_BOOST_MULTIPLIER = 1.5f;
    
    private boolean hasShield = false;
    private float shieldEndTime = 0f;
    private int shieldHealth = 0;
    private static final int SHIELD_MAX_HEALTH = 50;

    private int refill;

    public Player(float x, float y, float size, int id) {
        this.position = new Vector2(x, y);
        this.size = size;
        this.id = id;
        this.boundRect = new Rectangle(x, y, this.size, this.size);
        this.alive = true;
        this.health = 100;
        this.spikeCount = 0;
        this.refill = 0;
    }

    public void update(float deltaTime) {
        this.boundRect.x = position.x;
        this.boundRect.y = position.y;
    }

    /**
     * Update power-up effects and check for expiration.
     */
    public void updatePowerUpEffects(float currentTime) {
        // check if speed boost expired
        if (hasSpeedBoost && currentTime >= speedBoostEndTime) {
            hasSpeedBoost = false;
            speedMultiplier = 1.0f;
            System.out.println("Player " + id + " speed boost expired");
        }
        
        // check if damage boost expired
        if (hasDamageBoost && currentTime >= damageBoostEndTime) {
            hasDamageBoost = false;
            damageMultiplier = 1.0f;
            System.out.println("Player " + id + " damage boost expired");
        }
        
        // check if shield expired
        if (hasShield && currentTime >= shieldEndTime) {
            hasShield = false;
            shieldHealth = 0;
            System.out.println("Player " + id + " shield expired");
        }
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
        if (this.health > 100 ) this.health = 100;
    }

    public void hit(int damage) {
        // Shield absorbs damage first
        if (hasShield && shieldHealth > 0) {
            int damageToShield = Math.min(damage, shieldHealth);
            shieldHealth -= damageToShield;
            damage -= damageToShield;
            
            System.out.println("Player " + id + " shield absorbed " + damageToShield + 
                            " damage. Shield remaining: " + shieldHealth);
            
            if (shieldHealth <= 0) {
                hasShield = false;
                shieldHealth = 0;
                System.out.println("Player " + id + " shield broken!");
            }
        }
        
        // Apply remaining damage to health
        if (damage > 0) {
            this.health -= damage;
            if (this.health <= 0) {
                this.alive = false;
            }
        }

        // this.health -= damage;
        // if (this.health <= 0) {
        //     this.alive = false;
        // }
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

    //powerup methods
    /**
     * Apply speed boost power-up effect.
     * Increases movement speed by 2x for the duration.
     */
    public void applySpeedBoost(float currentTime, float duration) {
        this.hasSpeedBoost = true;
        this.speedBoostEndTime = currentTime + duration;
        this.speedMultiplier = SPEED_BOOST_MULTIPLIER;
        System.out.println("Player " + id + " activated SPEED BOOST (x" + SPEED_BOOST_MULTIPLIER + ") for " + duration + "s");
    }
    
    /**
     * Apply damage boost power-up effect.
     * Increases weapon damage by 1.5x for the duration.
     */
    public void applyDamageBoost(float currentTime, float duration) {
        this.hasDamageBoost = true;
        this.damageBoostEndTime = currentTime + duration;
        this.damageMultiplier = DAMAGE_BOOST_MULTIPLIER;
        System.out.println("Player " + id + " activated DAMAGE BOOST (x" + DAMAGE_BOOST_MULTIPLIER + ") for " + duration + "s");
    }
    
    /**
     * Apply shield power-up effect.
     * Provides 50 points of damage absorption for the duration.
     */
    public void applyShield(float currentTime, float duration) {
        this.hasShield = true;
        this.shieldEndTime = currentTime + duration;
        this.shieldHealth = SHIELD_MAX_HEALTH;
        System.out.println("Player " + id + " activated SHIELD (" + SHIELD_MAX_HEALTH + " HP) for " + duration + "s");
    }
    
    /**
     * Apply ammo refill power-up effect.
     * Instantly refills weapon ammo to full capacity.
     */
    public void applyAmmoRefill(float currentTime) {
        if (currentWeapon == null) {
            System.out.println("Player " + id + " has no weapon to refill");
            return;
        }

        currentWeapon.setAmmoCapacity(currentWeapon.getAmmoCapacity());
        System.out.println("Player " + id + " AMMO REFILLED to " + currentWeapon.getAmmoCapacity());
    }                                                                                                                           
    
    public float getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    public float getDamageMultiplier() {
        return damageMultiplier;
    }
    
    public boolean hasSpeedBoost() {
        return hasSpeedBoost;
    }

    public boolean hasDamageBoost() {
        return hasDamageBoost;
    }

    public boolean hasShield() {
        return hasShield;
    }

    public int getShieldHealth() {
        return shieldHealth;
    }

    public int getRefill() {
        return refill;
    }

    public float getSpeedBoostTimeRemaining(float currentTime) {
        if (!hasSpeedBoost) return 0f;
        return Math.max(0f, speedBoostEndTime - currentTime);
    }

    public float getDamageBoostTimeRemaining(float currentTime) {
        if (!hasDamageBoost) return 0f;
        return Math.max(0f, damageBoostEndTime - currentTime);
    }

    public float getShieldTimeRemaining(float currentTime) {
        if (!hasShield) return 0f;
        return Math.max(0f, shieldEndTime - currentTime);
    }

}
