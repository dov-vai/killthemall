package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.javakaian.shooter.achievements.AchievementManager;
import com.javakaian.shooter.logger.FileGameLoggerAdapter;
import com.javakaian.shooter.logger.GameLogEntry;
import com.javakaian.shooter.logger.IGameLogger;
import com.javakaian.states.State.StateEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * * This class is responsible for controlling states in a game. It invokes
 * render and update functions of current state and binds current states input
 * processor as a processor.
 * <p>
 * It keeps all the states of a game inside a HashMap. If player demands a state
 * which is not already in that hashmap, it creates that state and puts it to
 * the hashmap.
 *
 * @author oguz
 */
public class StateController {

    /**
     * A hashmap which stores states inside.
     */
    private Map<Integer, State> stateMap;
    /**
     * State object to store current state
     */
    private State currentState;
    /**
     * Ip address of the server
     */
    private String inetAddress;

    private AchievementManager achievementManager;

    private IGameLogger gameLogger;

    public StateController(String ip, AchievementManager achievementManager) {
        this.inetAddress = ip;
        stateMap = new HashMap<>();
        this.achievementManager = achievementManager;

        gameLogger = new FileGameLoggerAdapter("logs/state-transitions.log");

        GameLogEntry initEvent = new GameLogEntry(
                System.currentTimeMillis(),
                "CONTROLLER_INIT",
                "StateController initialized for server: " + ip,
                "INFO"
        );
        gameLogger.logEvent(initEvent);
    }

    /**
     * Sets the current state of the game to the given state. Takes StateEnum as a
     * parameter.
     *
     * @param stateEnum
     **/
    public void setState(StateEnum stateEnum) {
        String previousState = (currentState != null) ? currentState.getClass().getSimpleName() : "None";

        currentState = stateMap.get(stateEnum.ordinal());

        boolean isNewState = (currentState == null);

        if (currentState == null) {
            switch (stateEnum) {
                case PLAY_STATE:
                    currentState = new PlayState(this);
                    break;
                case GAME_OVER_STATE:
                    currentState = new GameOverState(this);
                    break;
                case MENU_STATE:
                    currentState = new MenuState(this);
                    break;
                case STATS_STATE:
                    currentState = new StatsState(this);
                    break;
                case ACHIEVEMENTS_STATE:
                    currentState = new AchievementsState(this, achievementManager);
                    break;
                case TEAM_SELECTION_STATE:
                    currentState = new TeamSelectionState(this);
                    break;
                default:
                    currentState = new MenuState(this);
                    break;
            }
            stateMap.put(stateEnum.ordinal(), currentState);
        }
        Gdx.input.setInputProcessor(currentState.ip);

        String newState = currentState.getClass().getSimpleName();
        GameLogEntry stateChangeEvent = new GameLogEntry(
                System.currentTimeMillis(),
                "STATE_CHANGE",
                "State transition: " + previousState + " -> " + newState +
                        (isNewState ? " (newly created)" : " (existing)"),
                "INFO"
        );
        gameLogger.logEvent(stateChangeEvent);
    }

    /**
     * Renders the current state.
     */
    public void render() {

        currentState.render();
    }

    /**
     * Updates the current state.
     */
    public void update(float deltaTime) {
        currentState.update(deltaTime);
    }

    /**
     * Calls the dispose method of each state in the hashmap.
     */
    public void dispose() {
        GameLogEntry disposeEvent = new GameLogEntry(
                System.currentTimeMillis(),
                "CONTROLLER_DISPOSE",
                "StateController disposing " + stateMap.size() + " states",
                "INFO"
        );
        gameLogger.logEvent(disposeEvent);

        stateMap.forEach((k, v) -> v.dispose());
    }

    /**
     * HashMap object which stores states of the game as key value pair.
     */
    public Map<Integer, State> getStateMap() {
        return stateMap;
    }

    /**
     * Returns the ip address of a server.
     */
    public String getInetAddress() {
        return inetAddress;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

}
