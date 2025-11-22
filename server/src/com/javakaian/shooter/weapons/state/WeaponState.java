package com.javakaian.shooter.weapons.state;

import com.javakaian.shooter.ServerWorld;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.weapons.Weapon;

public interface WeaponState {
    void attemptFire(Weapon weapon, ServerWorld world, Player owner, float angleRad);

    void attemptReload(Weapon weapon);

    void update(Weapon weapon, float deltaTime);

    String getName();
}
