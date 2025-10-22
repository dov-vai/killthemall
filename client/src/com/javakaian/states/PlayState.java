package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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

    public PlayState(StateController sc) {
        super(sc);

        themeFactory = ThemeFactory.getFactory(true); //fallback

        healthFont = GameUtils.generateBitmapFont(20, themeFactory.createTheme().getTextColor());
        notifFont = GameUtils.generateBitmapFont(24, Color.GOLD);
        weaponsFont = GameUtils.generateBitmapFont(14, Color.GRAY);

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
            WeaponChangeMessage message = new WeaponChangeMessage();
            message.setPlayerId(player.getId());
            message.setWeaponConfig(weaponConfig);
            client.sendTCP(message);
            
            // current weapon display
            currentWeaponInfo = weaponConfig.replace("_", " ").toUpperCase();
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
        }
    }
    
    public void undoSpike() {
        if (player != null) {
            UndoSpikeMessage message = new UndoSpikeMessage();
            message.setPlayerId(player.getId());
            client.sendTCP(message);
        }
    }

    public void setThemeFactory(ThemeFactory factory) {
        this.themeFactory = factory;

        Theme theme = factory.createTheme();

        if (aimLine != null) {
            aimLine.setColor(theme.getAimLineColor());
            aimLine.setCamera(camera);
        }

        if (healthFont != null) healthFont.dispose();
        healthFont = GameUtils.generateBitmapFont(20, theme.getTextColor());
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

        Color bg = themeFactory.createTheme().getBackgroundColor();
        ScreenUtils.clear(bg.r, bg.g, bg.b, 1);

        sr.begin(ShapeType.Line);
        sr.setColor(Color.RED);
        players.forEach(p -> p.render(sr));
        enemies.forEach(e -> e.render(sr));
        bullets.forEach(b -> b.render(sr));
        spikes.forEach(s -> s.render(sr));
        placedSpikes.forEach(ps -> ps.render(sr));
        sr.setColor(Color.BLUE);
        player.render(sr);
        aimLine.render(sr);
        followPlayer();
        sr.end();

        sb.begin();
        GameUtils.renderCenter("HEALTH: " + player.getHealth(), sb, healthFont, 0.1f);
        
        GameUtils.renderLeftAligned("WEAPON: " + currentWeaponInfo, sb, weaponsFont, 0.02f, 0.08f);
        if (currentWeaponComponents != null && !currentWeaponComponents.isEmpty()) {
            GameUtils.renderLeftAligned("Components: " + currentWeaponComponents, sb, weaponsFont, 0.02f, 0.11f);
        }
        if (currentWeaponStats != null && !currentWeaponStats.isEmpty()) {
            GameUtils.renderLeftAligned(currentWeaponStats, sb, weaponsFont, 0.02f, 0.14f);
        }
        GameUtils.renderLeftAligned("SPIKES: " + spikeCount, sb, weaponsFont, 0.02f, 0.17f);
        GameUtils.renderCenter("Press 1-4 for different weapons | E to place spike | U to undo", sb, healthFont, 0.95f);

        renderNotifications();
        sb.end();
    }

    private void renderNotifications() {
        float startY = 0.12f;
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
            GameStats.getInstance().addDistanceTraveled(dist);
            lastX = x;
            lastY = y;
        }

        processInputs();

        clearNotifications(deltaTime);
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

    public void shoot() {
        ShootMessage m = new ShootMessage();
        m.setPlayerId(player.getId());
        m.setAngleDeg(aimLine.getAngle());
        GameStats.getInstance().incrementShotsFired();
        client.sendUDP(m);
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
        GameStats.getInstance().resetSession();
        GameStats.getInstance().startSession();
        sc.getAchievementManager().addListener(this);
    }

    @Override
    public void logoutReceived(LogoutMessage m) {
        // do the logout proccess here
    }

    @Override
    public void playerDiedReceived(PlayerDiedMessage m) {
        if (player.getId() != m.getPlayerId())
            return;

        LogoutMessage mm = new LogoutMessage();
        mm.setPlayerId(player.getId());
        client.sendTCP(mm);
        client.close();
        GameStats.getInstance().incrementDeaths();
        GameStats.getInstance().endSession();
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
                        GameStats.getInstance().addDamageTaken(oldHealth - newHealth);
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
        GameStats.getInstance().endSession();
        sc.getAchievementManager().removeListener(this);
    }

    @Override
    public void onAchievementUnlocked(Achievement achievement) {
        notifications.add(new Notification("Achievement Unlocked: " + achievement.getTitle(), 3.0f));
    }
}
