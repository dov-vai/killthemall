package com.javakaian.shooter.weapons.state;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.weapons.Weapon;

public class ReloadingState implements WeaponState {
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

        if (timer >= 1.6f) {
            weapon.setCurrentAmmo(weapon.getAmmoCapacity());
            weapon.setState(new ReadyState());
        }
    }

    @Override
    public String getName() {
        return "RELOADING";
    }
}
