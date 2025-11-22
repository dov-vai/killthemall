package com.javakaian.shooter.weapons.state;

import com.javakaian.shooter.weapons.Weapon;

public interface WeaponListener {
    void onAmmoChanged(Weapon weapon);
}
