package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.states.GameOverState;
import com.javakaian.states.State.StateEnum;

/**
 * Input handles of Game Over State.
 *
 * @author oguz
 */
public class GameOverInput extends InputAdapter {

    private GameOverState gameOver;

    public GameOverInput(GameOverState game) {
        this.gameOver = game;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch  (keycode) {
            case Keys.R:
                gameOver.restart();
                gameOver.getSc().setState(StateEnum.PLAY_STATE);
                break;
            case Keys.M:
                gameOver.getSc().setState(StateEnum.MENU_STATE);
                break;
            default:
                break;
        }

        return true;
    }

}
