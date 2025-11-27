package com.javakaian.shooter.weapons.state;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.weapons.Weapon;

public class ReadyState implements WeaponState {

    @Override
    public void attemptFire(Weapon weapon, ServerWorld world, Player owner, float angleRad) {
        if (weapon.getCurrentAmmo() > 0) {
            weapon.fireWeapon(world, owner, angleRad);
            weapon.setState(new CooldownState());
        } else {
            weapon.setState(new EmptyState());
        }
    }

    @Override
    public void attemptReload(Weapon weapon) {
        if (weapon.getCurrentAmmo() < weapon.getAmmoCapacity()) {
            weapon.setState(new ReloadingState());
        }
    }

    @Override
    public void update(Weapon weapon, float deltaTime) {

    }

    @Override
    public String getName() {
        return "READY";
    }
}
