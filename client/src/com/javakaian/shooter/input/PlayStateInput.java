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
                playState.shoot();
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
            // Attachments toggles
            case Keys.NUM_4:
                // Toggle scope attachment
                playState.requestAttachmentChange("scope:4x ACOG:150");
                break;
            case Keys.NUM_5:
                // Toggle extended magazine
                playState.requestAttachmentChange("mag:15");
                break;
            case Keys.NUM_6:
                // Toggle grip
                playState.requestAttachmentChange("grip:Tactical:0.5");
                break;
            case Keys.NUM_7:
                // Toggle silencer
                playState.requestAttachmentChange("silencer:Silenced Barrel:2");
                break;
            case Keys.NUM_8:
                // Toggle damage boost
                playState.requestAttachmentChange("dmg:5");
                break;
            case Keys.NUM_0:
                // Reset attachments, keep current base
                playState.resetAttachments();
            case Keys.E:
                playState.placeSpike();
                break;
            case Keys.U:
                playState.undoSpike();
                break;
            default:
                break;
        }

        return true;
    }

}
