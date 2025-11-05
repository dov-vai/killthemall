package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.javakaian.models.Notification;
import com.javakaian.network.OClient;
import com.javakaian.network.messages.*;
import com.javakaian.network.messages.PositionMessage.Direction;
import com.javakaian.shooter.OMessageListener;
import com.javakaian.shooter.achievements.Achievement;
import com.javakaian.shooter.achievements.AchievementObserver;
import com.javakaian.shooter.ThemeFactory.Theme;
import com.javakaian.shooter.ThemeFactory.ThemeFactory;
import com.javakaian.shooter.input.PlayStateInput;
import com.javakaian.shooter.shapes.AimLine;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.shapes.Spike;
import com.javakaian.shooter.shapes.PlacedSpike;
import com.javakaian.shooter.utils.*;
import com.javakaian.shooter.utils.Subsystems.StatAction;
import com.javakaian.shooter.utils.Subsystems.StatType;
import com.javakaian.shooter.utils.Subsystems.TextAlignment;

import com.javakaian.shooter.logger.*;
import com.javakaian.shooter.utils.stats.GameStats;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the state where gameplay happens.
 *
 * @author oguz
 */
public class PlayState extends State implements OMessageListener, AchievementObserver {

    private ThemeFactory themeFactory;
    private Player player;
    private List<Player> players;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private List<Spike> spikes;
    private List<PlacedSpike> placedSpikes;
    private AimLine aimLine;
    GameManagerFacade stats = GameManagerFacade.getInstance();


    private OClient client;

    private BitmapFont healthFont;
    private float lastX, lastY;

    private final List<Notification> notifications = new ArrayList<>();
    private BitmapFont notifFont;

    private String currentWeaponInfo = "Assault rifle";
    private String currentWeaponComponents = "";
    private String currentWeaponStats = "";
    private BitmapFont weaponsFont;
    
    private int spikeCount = 0;

    // Adapter pattern - unified game logger
    private IGameLogger gameLogger;
    private SimpleLogDisplay logDisplay;

    // Track current base weapon and active attachments to send full config to server
    private String currentBaseConfig = "assault_rifle";
    private final List<String> activeAttachments = new ArrayList<>();

    public PlayState(StateController sc) {
        super(sc);

        themeFactory = ThemeFactory.getFactory(true); //fallback

        healthFont = GameManagerFacade.getInstance().generateBitmapFont(20, themeFactory.createTheme().getTextColor());
        notifFont = GameManagerFacade.getInstance().generateBitmapFont(24, Color.GOLD);
        weaponsFont = GameManagerFacade.getInstance().generateBitmapFont(14, Color.GRAY);

        gameLogger = new ConsoleGameLoggerAdapter();
        logDisplay = new SimpleLogDisplay();

        init();
        ip = new PlayStateInput(this);
    }

    @Override
    public void weaponInfoReceived(WeaponInfoMessage m) {
        if (player != null && m.getPlayerId() == player.getId()) {
            currentWeaponInfo = m.getWeaponName();
            currentWeaponComponents = m.getComponents();
            currentWeaponStats = m.getStats();
        }
    }

    public void requestWeaponChange(String weaponConfig) {
        if (player != null) {
            // Set base config and clear attachments on base change
            currentBaseConfig = extractBaseConfig(weaponConfig);
            activeAttachments.clear();

            WeaponChangeMessage message = new WeaponChangeMessage();
            message.setPlayerId(player.getId());
            message.setWeaponConfig(weaponConfig);
            client.sendTCP(message);
            
            // current weapon display
            currentWeaponInfo = weaponConfig.replace("_", " ").toUpperCase();

            GameLogEntry weaponChangeEvent = new GameLogEntry(
                System.currentTimeMillis(),
                "WEAPON_CHANGE",
                "Player " + player.getId() + " changed weapon to " + currentWeaponInfo,
                "INFO"
            );
            gameLogger.logEvent(weaponChangeEvent);
        }
    }
    
    public void placeSpike() {
        if (player != null && spikeCount > 0) {
            PlaceSpikeMessage message = new PlaceSpikeMessage();
            message.setPlayerId(player.getId());
            // Get rotation from aimline
            float rotation = (float) Math.toDegrees(aimLine.getAngle());
            message.setRotation(rotation);
            client.sendTCP(message);

            GameLogEntry spikeEvent = new GameLogEntry(
                System.currentTimeMillis(),
                "SPIKE_PLACED",
                "Player " + player.getId() + " placed spike at rotation " + String.format("%.2f", rotation) + "°",
                "INFO"
            );
            gameLogger.logEvent(spikeEvent);
        }
    }
    
    public void undoSpike() {
        if (player != null) {
            UndoSpikeMessage message = new UndoSpikeMessage();
            message.setPlayerId(player.getId());
            client.sendTCP(message);
        }
    }

    // Toggle attachment by spec, then send combined config: base+att1+att2...
    public void requestAttachmentChange(String attachmentSpec) {
        if (player == null) return;
        toggleAttachment(attachmentSpec);
        sendCombinedConfig();
    }

    public void resetAttachments() {
        if (player == null) return;
        activeAttachments.clear();
        sendCombinedConfig();
    }

    private void toggleAttachment(String spec) {
        if (activeAttachments.contains(spec)) {
            activeAttachments.remove(spec);
        } else {
            activeAttachments.add(spec);
        }
    }

    private void sendCombinedConfig() {
        StringBuilder cfg = new StringBuilder(currentBaseConfig);
        for (String att : activeAttachments) {
            cfg.append("+").append(att);
        }
        WeaponChangeMessage message = new WeaponChangeMessage();
        message.setPlayerId(player.getId());
        message.setWeaponConfig(cfg.toString());
        client.sendTCP(message);
    }

    private String extractBaseConfig(String full) {
        if (full == null || full.isEmpty()) return "assault_rifle";
        int idx = full.indexOf('+');
        if (idx < 0) return full;
        return full.substring(0, idx);
    }

    public void setThemeFactory(ThemeFactory factory) {
        this.themeFactory = factory;

        Theme theme = factory.createTheme();

        if (aimLine != null) {
            aimLine.setColor(theme.getAimLineColor());
            aimLine.setCamera(camera);
        }

        if (healthFont != null) healthFont.dispose();
        healthFont = GameManagerFacade.getInstance().generateBitmapFont(20, theme.getTextColor());
    }


    private void init() {
        client = new OClient(sc.getInetAddress(), this);
        client.connect();

        players = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        spikes = new ArrayList<>();
        placedSpikes = new ArrayList<>();

        aimLine = themeFactory.createAimLine(new Vector2(0, 0), new Vector2(0, 0));
        aimLine.setCamera(camera);

        LoginMessage m = new LoginMessage();
        m.setX(new SecureRandom().nextInt(GameConstants.SCREEN_WIDTH));
        m.setY(new SecureRandom().nextInt(GameConstants.SCREEN_HEIGHT));
        client.sendTCP(m);
    }

    @Override
    public void render() {
        sr.setProjectionMatrix(camera.combined);
        camera.update();

        if (player == null) return;

        followPlayer();
        Color bg = themeFactory.createTheme().getBackgroundColor();
        ScreenUtils.clear(bg.r, bg.g, bg.b, 1);

        GameManagerFacade gm = GameManagerFacade.getInstance();
        gm.renderGameObjects(sr, players, enemies, bullets, spikes, placedSpikes, player, aimLine);

        sb.begin();
        gm.renderText(sb, healthFont, "HEALTH: " + player.getHealth(), TextAlignment.CENTER, 0f, 0.05f);

        float baseEquipmentY = 0.05f;

        gm.renderText(sb, weaponsFont, "WEAPON: " + currentWeaponInfo, TextAlignment.LEFT, 0.02f,  baseEquipmentY + 0.08f);

        if (currentWeaponComponents != null && !currentWeaponComponents.isEmpty()) {
            gm.renderText(sb, weaponsFont, "Components: " + currentWeaponComponents, TextAlignment.LEFT, 0.02f, baseEquipmentY + 0.11f);
        }
        if (currentWeaponStats != null && !currentWeaponStats.isEmpty()) {
            gm.renderText(sb, weaponsFont, currentWeaponStats, TextAlignment.LEFT, 0.02f, baseEquipmentY + 0.14f);
        }

        gm.renderText(sb, weaponsFont, "SPIKES: " + spikeCount, TextAlignment.LEFT, 0.02f, baseEquipmentY + 0.17f);
        gm.renderText(
                sb,
                healthFont,
                "1-3: Weapons | 4: Scope | 5: Mag | 6: Grip | 7: Silencer | 8: Dmg | 0: Reset attachments",
                TextAlignment.CENTER,
                0f,
                0.90f
        );

        gm.renderText(
                sb,
                healthFont,
                "E to place spike | U to undo | L: Logs",
                TextAlignment.CENTER,
                0f,
                0.95f
        );


        renderNotifications();
        sb.end();

        // Render log display if visible
        if (logDisplay != null && logDisplay.isVisible()) {
            logDisplay.render(sb);
        }
    }

    private void renderNotifications() {
        float startY = 0.15f;
        float y = startY;
        for (int i = 0; i < notifications.size(); i++) {
            Notification n = notifications.get(i);
            GameUtils.renderCenter(n.text, sb, notifFont, y);
            y += 0.05f;
            if (i >= 3) break; // show up to 4
        }
    }

    private void followPlayer() {
        float lerp = 0.05f;
        camera.position.x += (player.getPosition().x - camera.position.x) * lerp;
        camera.position.y += (player.getPosition().y - camera.position.y) * lerp;
    }

    @Override
    public void update(float deltaTime) {
        if (player == null)
            return;
        aimLine.setBegin(player.getCenter());
        aimLine.update(deltaTime);
        // track distance traveled
        float x = player.getPosition().x;
        float y = player.getPosition().y;
        float dx = x - lastX;
        float dy = y - lastY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist > 0) {
            stats.stats(StatAction.SET, StatType.TOTAL_DISTANCE, dist);
            lastX = x;
            lastY = y;
        }

        processInputs();

        clearNotifications(deltaTime);

        // Update log display
        if (logDisplay != null) {
            logDisplay.update(deltaTime);
        }
    }

    private void clearNotifications(float deltaTime) {
        for (int i = notifications.size() - 1; i >= 0; i--) {
            Notification n = notifications.get(i);
            n.ttl -= deltaTime;
            if (n.ttl <= 0) notifications.remove(i);
        }
    }

    public void scrolled(float amountY) {
        if (amountY > 0) {
            camera.zoom += 0.2F;
        } else if (camera.zoom >= 0.4) {
            camera.zoom -= 0.2F;
        }
    }

    public void toggleLogDisplay() {
        if (logDisplay != null) {
            logDisplay.toggle();
        }
    }

    public void shoot() {
        ShootMessage m = new ShootMessage();
        m.setPlayerId(player.getId());
        m.setAngleDeg(aimLine.getAngle());
        stats.stats(StatAction.SET, StatType.TOTAL_SHOTS);
        client.sendUDP(m);

        GameLogEntry shootEvent = new GameLogEntry(
            System.currentTimeMillis(),
            "PLAYER_SHOOT",
            "Player " + player.getId() + " fired at angle " + String.format("%.2f", Math.toDegrees(aimLine.getAngle())) + "°",
            "DEBUG"
        );
        gameLogger.logEvent(shootEvent);
    }

    private void processInputs() {
        PositionMessage p = new PositionMessage();
        p.setPlayerId(player.getId());
        if (Gdx.input.isKeyPressed(Keys.S)) {
            p.setDirection(Direction.DOWN);
            client.sendUDP(p);
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            p.setDirection(Direction.UP);
            client.sendUDP(p);
        }
        if (Gdx.input.isKeyPressed(Keys.A)) {
            p.setDirection(Direction.LEFT);
            client.sendUDP(p);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            p.setDirection(Direction.RIGHT);
            client.sendUDP(p);
        }
    }

    @Override
    public void loginReceived(LoginMessage m) {
        player = new Player(m.getX(), m.getY(), 50);
        player.setId(m.getPlayerId());
        lastX = player.getPosition().x;
        lastY = player.getPosition().y;
        stats.startSession();
        sc.getAchievementManager().addListener(this);

        GameLogEntry loginEvent = new GameLogEntry(
            System.currentTimeMillis(),
            "PLAYER_LOGIN",
            "Player " + m.getPlayerId() + " joined at position (" + m.getX() + ", " + m.getY() + ")",
            "INFO"
        );
        gameLogger.logEvent(loginEvent);
    }

    @Override
    public void logoutReceived(LogoutMessage m) {
        // do the logout proccess here
    }

    @Override
    public void playerDiedReceived(PlayerDiedMessage m) {
        if (player.getId() != m.getPlayerId())
            return;

        GameLogEntry deathEvent = new GameLogEntry(
            System.currentTimeMillis(),
            "PLAYER_DEATH",
            "Player " + player.getId() + " died. Time alive: " + GameStats.getInstance().getTimeAliveSeconds() + "s",
            "WARN"
        );
        gameLogger.logEvent(deathEvent);

        LogoutMessage mm = new LogoutMessage();
        mm.setPlayerId(player.getId());
        client.sendTCP(mm);
        client.close();
        stats.endSession();
        this.getSc().setState(StateEnum.GAME_OVER_STATE);
    }

    @Override
    public void gwmReceived(GameWorldMessage m) {
        if (themeFactory == null) {
            themeFactory = ThemeFactory.getFactory(true);
            setThemeFactory(themeFactory);
        }

        enemies = themeFactory.createEnemiesFromGWM(m);
        bullets = OMessageParser.getBulletsFromGWM(m);
        players = OMessageParser.getPlayersFromGWM(m);
        spikes = OMessageParser.getSpikesFromGWM(m);
        placedSpikes = OMessageParser.getPlacedSpikesFromGWM(m);

        if (player == null) return;

        float oldHealth = player.getHealth();
        players.stream().filter(p -> p.getId() == player.getId())
                .findFirst()
                .ifPresent(p -> {
                    player = p;
                    float newHealth = player.getHealth();
                    if (newHealth < oldHealth) {
                        stats.stats(StatAction.SET, StatType.TOTAL_DAMAGE, oldHealth - newHealth);
                    }
                });
        players.removeIf(p -> p.getId() == player.getId());
    }
    
    @Override
    public void inventoryUpdateReceived(InventoryUpdateMessage m) {
        if (player != null && m.getPlayerId() == player.getId()) {
            spikeCount = m.getSpikeCount();
        }
    }

    public void restart() {
        init();
    }

    @Override
    public void dispose() {
        if (player != null) {
            LogoutMessage m = new LogoutMessage();
            m.setPlayerId(player.getId());
            client.sendTCP(m);
        }
        if (healthFont != null) healthFont.dispose();
        if (notifFont != null) notifFont.dispose();
        if (logDisplay != null) logDisplay.dispose();
        stats.endSession();

        sc.getAchievementManager().removeListener(this);
    }

    @Override
    public void onAchievementUnlocked(Achievement achievement) {
        notifications.add(new Notification("Achievement Unlocked: " + achievement.getTitle(), 3.0f));
    }
}
