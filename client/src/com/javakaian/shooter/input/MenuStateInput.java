package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.ThemeFactory.ThemeFactory;
import com.javakaian.states.MenuState;
import com.javakaian.states.PlayState;
import com.javakaian.states.State.StateEnum;

/**
 * Input Handles of MenuState
 *
 * @author oguz
 */
public class MenuStateInput extends InputAdapter {

    private final MenuState menuState;

    public MenuStateInput(MenuState menuState) {
        this.menuState = menuState;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Keys.SPACE:
                PlayState playState = (PlayState) menuState.getSc()
                        .getStateMap()
                        .get(StateEnum.PLAY_STATE.ordinal());

                if (playState == null) {
                    playState = new PlayState(menuState.getSc());
                    menuState.getSc().getStateMap().put(StateEnum.PLAY_STATE.ordinal(), playState);
                }

                playState.setThemeFactory(ThemeFactory.getFactory(menuState.isDarkMode()));

                menuState.getSc().setState(StateEnum.PLAY_STATE);
                break;

            case Keys.S:
                menuState.getSc().setState(StateEnum.STATS_STATE);
                break;

            case Keys.A:
                menuState.getSc().setState(StateEnum.ACHIEVEMENTS_STATE);
                break;

            case Keys.Q:
                menuState.quit();
                break;

            case Keys.RIGHT:
                menuState.toggleDarkMode();
                break;

            default:
                break;
        }

        return true;
    }
}
