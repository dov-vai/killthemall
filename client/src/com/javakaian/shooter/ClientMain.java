package com.javakaian.shooter;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.javakaian.shooter.utils.GameConstants;
import org.apache.log4j.Logger;

public class ClientMain {

    private static Logger logger = Logger.getLogger(ClientMain.class);

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        config.setForegroundFPS(60);
        config.setIdleFPS(60);
        config.setResizable(false);
        // config.x = 2500;

        String ip = null;
        if (arg.length == 0) {
            logger.debug("No arg has been passed. LOCALHOST ip.");
            ip = "localhost";
        } else {
            ip = arg[0];
        }
        new Lwjgl3Application(new KillThemAll(ip), config);
    }
}
