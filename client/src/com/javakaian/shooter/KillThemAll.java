package com.javakaian.shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.javakaian.states.State.StateEnum;
import com.javakaian.states.StateController;
import com.javakaian.shooter.achievements.AchievementManager;
import com.javakaian.shooter.utils.GameStats;

public class KillThemAll extends ApplicationAdapter {

    private StateController sc;

    private String inetAddress;

    public KillThemAll(String inetAddress) {
        this.inetAddress = inetAddress;
    }

    @Override
    public void create() {
        var achievementManager = new AchievementManager();
        sc = new StateController(inetAddress, achievementManager);
        sc.setState(StateEnum.MENU_STATE);
    }

    @Override
    public void render() {
        sc.render();
        sc.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        super.dispose();
        GameStats.getInstance().save();
        sc.dispose();
    }

}
