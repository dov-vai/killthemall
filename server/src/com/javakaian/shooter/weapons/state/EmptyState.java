package com.javakaian.shooter.weapons.state;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.weapons.Weapon;

public class EmptyState implements WeaponState {
    @Override
    public void attemptFire(Weapon weapon, ServerWorld world, Player owner, float angleRad) {

    }

    @Override
    public void attemptReload(Weapon weapon) {
        weapon.setState(new ReloadingState());
    }

    @Override
    public void update(Weapon weapon, float deltaTime) {

    }

    @Override
    public String getName() {
        return "EMPTY";
    }
}
