package com.javakaian.shooter.weapons.state;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.weapons.Weapon;

public class CooldownState implements WeaponState {
    private float timer = 0f;

    @Override
    public void attemptFire(Weapon weapon, ServerWorld world, Player owner, float angleRad) {

    }

    @Override
    public void attemptReload(Weapon weapon) {

    }

    @Override
    public void update(Weapon weapon, float deltaTime) {
        timer += deltaTime;

        float requiredDelay = 1.0f / weapon.getFireRate();

        if (timer >= requiredDelay) {
            if (weapon.getCurrentAmmo() <= 0) {
                weapon.setState(new EmptyState());
            } else {
                weapon.setState(new ReadyState());
            }
        }
    }

    @Override
    public String getName() {
        return "COOLDOWN";
    }
}
