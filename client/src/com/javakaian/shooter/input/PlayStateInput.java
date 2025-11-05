package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.utils.stats.GameStats;
import com.javakaian.states.PlayState;
import com.javakaian.states.State.StateEnum;

/**
 * Input handles of PlayState
 *
 * @author oguz
 */
public class PlayStateInput extends InputAdapter {

    private PlayState playState;

    public PlayStateInput() {
    }

    public PlayStateInput(PlayState playState) {
        this.playState = playState;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        playState.scrolled(amountY);

        return super.scrolled(amountX, amountY);
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Keys.SPACE:
                playState.startFiring();
                break;
            case Keys.M:
                GameStats.getInstance().endSession();
                playState.getSc().setState(StateEnum.MENU_STATE);
                break;
            case Keys.NUM_1:
                playState.requestWeaponChange("assault_rifle");
                break;
            case Keys.NUM_2:
                playState.requestWeaponChange("combat_shotgun");
                break;
            case Keys.NUM_3:
                playState.requestWeaponChange("precision_sniper");
                break;
            case Keys.E:
                playState.placeSpike();
                break;
            case Keys.U:
                playState.undoSpike();
                break;
            // Bridge Pattern: Control Mode switches
            case Keys.F1:
                playState.switchControlMode("manual");
                break;
            case Keys.F2:
                playState.switchControlMode("autoaim");
                break;
            // Bridge Pattern: Firing Mode switches
            case Keys.F3:
                playState.switchFiringMode("semiauto");
                break;
            case Keys.F4:
                playState.switchFiringMode("burst");
                break;
            case Keys.F5:
                playState.switchFiringMode("fullauto");
                break;
            // Bridge Pattern: Special actions
            case Keys.Q:
                playState.useSpecialAction();
                break;
            case Keys.R:
                playState.reloadWeapon();
                break;
            default:
                break;
        }

        return true;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.SPACE:
                playState.stopFiring();
                break;
            default:
                break;
        }
        return true;
    }

}
