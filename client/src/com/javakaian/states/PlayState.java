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
import com.javakaian.shooter.ThemeFactory.Theme;
import com.javakaian.shooter.ThemeFactory.ThemeFactory;
import com.javakaian.shooter.achievements.Achievement;
import com.javakaian.shooter.achievements.AchievementObserver;
import com.javakaian.shooter.input.PlayStateInput;
import com.javakaian.shooter.logger.ConsoleGameLoggerAdapter;
import com.javakaian.shooter.logger.GameLogEntry;
import com.javakaian.shooter.logger.IGameLogger;
import com.javakaian.shooter.logger.SimpleLogDisplay;
import com.javakaian.shooter.shapes.*;
import com.javakaian.shooter.utils.GameConstants;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.OMessageParser;
import com.javakaian.shooter.utils.Subsystems.StatAction;
import com.javakaian.shooter.utils.Subsystems.StatType;
import com.javakaian.shooter.utils.Subsystems.TextAlignment;
import com.javakaian.shooter.utils.stats.GameStats;
import com.javakaian.shooter.weapons.bridge.*;
import com.javakaian.shooter.utils.fonts.FontManager;
import com.javakaian.shooter.weapons.images.WeaponImage;
import com.javakaian.shooter.weapons.images.WeaponImageFactory;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the state where gameplay happens.
 *
 * @author oguz
 */
public class PlayState extends State implements OMessageListener, AchievementObserver {

    private static final float BURST_DELAY = 0.1f; // Delay between burst shots
    private static final float AUTO_FIRE_RATE = 0.1f; // Time between auto shots
    private final List<Notification> notifications = new ArrayList<>();
    private final List<String> activeAttachments = new ArrayList<>();
    private ThemeFactory themeFactory;
    private Player player;
    private List<Player> players;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private List<Spike> spikes;
    private List<PlacedSpike> placedSpikes;
    private AimLine aimLine;
    GameManagerFacade stats = GameManagerFacade.getInstance();
    private List<PowerUp> powerUps;
    private boolean hasShield = false;
    private OClient client;
    //private BitmapFont healthFont;
    private float lastX, lastY;
    //private BitmapFont notifFont;
    private String currentWeaponInfo = "Assault rifle";
    private String currentWeaponComponents = "";
    private String currentWeaponStats = "";
    //private BitmapFont weaponsFont;
    private int spikeCount = 0;
    // Adapter pattern - unified game logger
    private IGameLogger gameLogger;
    private SimpleLogDisplay logDisplay;
    // Track current base weapon and active attachments to send full config to
    // server
    private String currentBaseConfig = "assault_rifle";
    // Bridge Pattern - Weapon system
    private BridgeWeapon currentBridgeWeapon;
    private BridgeWeapon unwrappedWeapon; // Base weapon without decorators
    private FiringMechanism currentFiringMode;
    private boolean isShooting = false;
    private int burstShotsRemaining = 0;
    private float burstShotTimer = 0;
    private float autoFireTimer = 0;

    private FontManager healthFontManager;
    private FontManager notifFontManager;
    private FontManager weaponsFontManager;
    
    // Flyweight Pattern - Weapon images
    private WeaponImageFactory weaponImageFactory;
    private WeaponImage currentWeaponImage;
    
    // Team chat - Mediator pattern
    private String selectedTeam;
    private java.util.List<ChatMessage> chatMessages;
    private static final int MAX_CHAT_MESSAGES = 10;
    private boolean chatInputActive = false;
    private boolean justOpenedChat = false; // Flag to prevent 'T' from appearing when opening chat
    private StringBuilder chatInputText = new StringBuilder();
    private BitmapFont chatFont;
    private BitmapFont chatFontRed;
    private BitmapFont chatFontBlue;
    private BitmapFont chatFontGreen;


    public PlayState(StateController sc) {
        super(sc);

        themeFactory = ThemeFactory.getFactory(true); // fallback

        // healthFont = GameManagerFacade.getInstance().generateBitmapFont(20, themeFactory.createTheme().getTextColor());
        // notifFont = GameManagerFacade.getInstance().generateBitmapFont(24, Color.GOLD);
        // weaponsFont = GameManagerFacade.getInstance().generateBitmapFont(14, Color.GRAY);

        // 1. Health font - Virtual Proxy (delayed creation)
        healthFontManager = new FontManager(20, themeFactory.createTheme().getTextColor(), 
            "Warungasem.ttf", "PlayState-HealthFont");
        healthFontManager.switchToVirtualProxy();

        // 2. Notification font - Caching Proxy (performance)
        notifFontManager = new FontManager(24, Color.GOLD, "Warungasem.ttf", "PlayState-NotifFont");
        notifFontManager.switchToCachingProxy();

        // 3. Weapons font - Security Proxy (restricted access)
        weaponsFontManager = new FontManager(14, Color.GRAY, "Warungasem.ttf", "PlayState-WeaponsFont");
        weaponsFontManager.switchToSecurityProxy(true); // Start with access allowed

        gameLogger = new ConsoleGameLoggerAdapter();
        logDisplay = new SimpleLogDisplay();

        // Initialize Bridge Pattern weapons
        currentFiringMode = new SingleShotMechanism();
        currentBridgeWeapon = new AssaultRifle(currentFiringMode);
        
        // Initialize Flyweight Pattern for weapon images
        weaponImageFactory = WeaponImageFactory.getInstance();
        currentWeaponImage = weaponImageFactory.getWeaponImage("assault_rifle");
        
        // Initialize chat fonts once to prevent memory leak
        chatFont = GameManagerFacade.getInstance().generateBitmapFont(14, Color.WHITE);
        chatFontRed = GameManagerFacade.getInstance().generateBitmapFont(14, Color.RED);
        chatFontBlue = GameManagerFacade.getInstance().generateBitmapFont(14, Color.CYAN);
        chatFontGreen = GameManagerFacade.getInstance().generateBitmapFont(14, Color.GREEN);

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

            // Bridge Pattern - Update local weapon
            updateBridgeWeapon(weaponConfig);
            
            // Flyweight Pattern - Load weapon image (cached if previously loaded)
            long imageLoadStart = System.nanoTime();
            currentWeaponImage = weaponImageFactory.getWeaponImage(currentBaseConfig);
            currentWeaponImage.getTexture(); // Trigger loading
            long imageLoadTime = System.nanoTime() - imageLoadStart;
            
            // Log image loading performance
            GameLogEntry imageLoadEvent = new GameLogEntry(
                    System.currentTimeMillis(),
                    "WEAPON_IMAGE_LOAD",
                    String.format("Loaded image for %s in %.3f ms (Flyweight cache)", 
                        currentBaseConfig, imageLoadTime / 1_000_000.0),
                    "INFO");
            gameLogger.logEvent(imageLoadEvent);

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
                    "INFO");
            gameLogger.logEvent(weaponChangeEvent);

            // Show notification
            notifications.add(new Notification(
                    "Equipped: " + currentBridgeWeapon.getName() +
                            " [" + currentFiringMode.getDescription() + "]",
                    2.0f));
        }
    }

    // Bridge Pattern - Update weapon based on config
    private void updateBridgeWeapon(String weaponConfig) {
        String baseWeapon = extractBaseConfig(weaponConfig);

        switch (baseWeapon) {
            case "assault_rifle":
                unwrappedWeapon = new AssaultRifle(currentFiringMode);
                break;
            case "combat_shotgun":
                unwrappedWeapon = new TacticalShotgun(currentFiringMode);
                break;
            case "precision_sniper":
                unwrappedWeapon = new PrecisionSniper(currentFiringMode);
                break;
            default:
                unwrappedWeapon = new AssaultRifle(currentFiringMode);
                break;
        }

        // Set current weapon to unwrapped (will be wrapped with decorators if
        // attachments exist)
        currentBridgeWeapon = unwrappedWeapon;

        // Reapply any active decorators
        if (!activeAttachments.isEmpty()) {
            applyDecoratorsToCurrentWeapon();
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
                    "INFO");
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
        if (player == null)
            return;
        toggleAttachment(attachmentSpec);

        // Apply decorators to Bridge weapon
        applyDecoratorsToCurrentWeapon();

        sendCombinedConfig();
    }

    public void resetAttachments() {
        if (player == null)
            return;
        activeAttachments.clear();

        // Reset to unwrapped weapon
        if (unwrappedWeapon != null) {
            currentBridgeWeapon = unwrappedWeapon;
        }

        sendCombinedConfig();
    }

    // Apply decorators based on active attachments
    private void applyDecoratorsToCurrentWeapon() {
        if (unwrappedWeapon == null)
            return;

        // Start with base weapon
        BridgeWeapon weapon = unwrappedWeapon;

        // Apply each active attachment as a decorator
        for (String spec : activeAttachments) {
            weapon = applyDecorator(weapon, spec);
        }

        currentBridgeWeapon = weapon;

        // Show notification
        if (!activeAttachments.isEmpty()) {
            notifications.add(new Notification(
                    "Attachments: " + activeAttachments.size() + " active",
                    1.5f));
        }
    }

    // Apply a single decorator based on spec
    private BridgeWeapon applyDecorator(BridgeWeapon weapon, String spec) {
        // Parse spec format: "type:name:value" or "type:value"
        String[] parts = spec.split(":");
        if (parts.length == 0)
            return weapon;

        String type = parts[0];

        switch (type) {
            case "scope":
                // scope:4x ACOG:150
                if (parts.length >= 3) {
                    String scopeName = parts[1];
                    float rangeBonus = Float.parseFloat(parts[2]);
                    return new ScopeDecorator(weapon, scopeName, rangeBonus);
                }
                break;
            case "mag":
                // mag:15
                if (parts.length >= 2) {
                    int extraAmmo = Integer.parseInt(parts[1]);
                    return new ExtendedMagDecorator(weapon, extraAmmo);
                }
                break;
            case "grip":
                // grip:Tactical:0.5
                if (parts.length >= 3) {
                    String gripName = parts[1];
                    float fireRateBonus = Float.parseFloat(parts[2]);
                    return new GripDecorator(weapon, gripName, fireRateBonus);
                }
                break;
            case "silencer":
                // silencer:Silenced Barrel:2
                if (parts.length >= 3) {
                    String barrelName = parts[1];
                    float damagePenalty = Float.parseFloat(parts[2]);
                    return new SilencerDecorator(weapon, barrelName, damagePenalty);
                }
                break;
            case "dmg":
                // dmg:5
                if (parts.length >= 2) {
                    float damageBonus = Float.parseFloat(parts[1]);
                    return new DamageBoostDecorator(weapon, damageBonus);
                }
                break;
        }

        return weapon;
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
        if (full == null || full.isEmpty())
            return "assault_rifle";
        int idx = full.indexOf('+');
        if (idx < 0)
            return full;
        return full.substring(0, idx);
    }

    public void setThemeFactory(ThemeFactory factory) {
        this.themeFactory = factory;

        Theme theme = factory.createTheme();

        if (aimLine != null) {
            aimLine.setColor(theme.getAimLineColor());
            aimLine.setCamera(camera);
        }

        // if (healthFont != null)
        //     healthFont.dispose();
        // healthFont = GameManagerFacade.getInstance().generateBitmapFont(20, theme.getTextColor());
        healthFontManager.dispose();
        healthFontManager = new FontManager(20, theme.getTextColor(), "Warungasem.ttf", "PlayState-HealthFont");
        healthFontManager.switchToVirtualProxy();
    }

    private void init() {
        client = new OClient(sc.getInetAddress(), this);
        client.connect();

        players = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        spikes = new ArrayList<>();
        placedSpikes = new ArrayList<>();
        powerUps = new ArrayList<>();

        aimLine = themeFactory.createAimLine(new Vector2(0, 0), new Vector2(0, 0));
        aimLine.setCamera(camera);

        chatMessages = new java.util.ArrayList<>();
    }
    
    private void connectToServer() {
        LoginMessage m = new LoginMessage();
        m.setX(new SecureRandom().nextInt(GameConstants.SCREEN_WIDTH));
        m.setY(new SecureRandom().nextInt(GameConstants.SCREEN_HEIGHT));
        m.setSelectedTeam(selectedTeam != null ? selectedTeam : "RED");
        client.sendTCP(m);
    }

    @Override
    public void render() {
        sr.setProjectionMatrix(camera.combined);
        camera.update();

        if (player == null)
            return;

        BitmapFont healthFont = healthFontManager.getFontResource().getFont();
        BitmapFont weaponsFont = weaponsFontManager.getFontResource().getFont();

        followPlayer();
        Color bg = themeFactory.createTheme().getBackgroundColor();
        ScreenUtils.clear(bg.r, bg.g, bg.b, 1);

        GameManagerFacade gm = GameManagerFacade.getInstance();
        gm.renderGameObjects(sr, players, enemies, bullets, spikes, placedSpikes, powerUps, player, aimLine);

        sb.begin();
        gm.renderText(sb, healthFont, "HEALTH: " + player.getHealth(), TextAlignment.CENTER, 0f, 0.05f);

        if (player.hasShield()) {
            gm.renderText(sb, weaponsFont, 
                "SHIELD: " + player.getShieldHealth() + " HP", 
                TextAlignment.CENTER, 0f, 0.08f);
        }

        float baseEquipmentY = 0.05f;

        // Bridge Pattern - Display weapon info
        if (currentBridgeWeapon != null) {
            gm.renderText(sb, weaponsFont,
                    "WEAPON: " + currentBridgeWeapon.getName(),
                    TextAlignment.LEFT, 0.02f, baseEquipmentY + 0.08f);

            // Display equipped attachments
            if (!activeAttachments.isEmpty()) {
                StringBuilder attachmentsList = new StringBuilder("ATTACHMENTS: ");
                for (int i = 0; i < activeAttachments.size(); i++) {
                    String spec = activeAttachments.get(i);
                    String[] parts = spec.split(":");
                    String attachmentName = parts.length > 0 ? parts[0].toUpperCase() : "?";
                    attachmentsList.append(attachmentName);
                    if (i < activeAttachments.size() - 1) {
                        attachmentsList.append(", ");
                    }
                }
                gm.renderText(sb, weaponsFont,
                        attachmentsList.toString(),
                        TextAlignment.LEFT, 0.02f, baseEquipmentY + 0.11f);
            }

            gm.renderText(sb, weaponsFont,
                    "MODE: " + currentFiringMode.getDescription(),
                    TextAlignment.LEFT, 0.02f,
                    activeAttachments.isEmpty() ? baseEquipmentY + 0.11f : baseEquipmentY + 0.14f);

            gm.renderText(sb, weaponsFont,
                    "AMMO: " + currentBridgeWeapon.getCurrentAmmo() + "/" +
                            currentBridgeWeapon.getAmmoCapacity(),
                    TextAlignment.LEFT, 0.02f,
                    activeAttachments.isEmpty() ? baseEquipmentY + 0.14f : baseEquipmentY + 0.17f);

            gm.renderText(sb, weaponsFont,
                    String.format("DMG: %.0f | RNG: %.0f | RATE: %.2f",
                            currentBridgeWeapon.getDamage(),
                            currentBridgeWeapon.getRange(),
                            currentBridgeWeapon.getEffectiveFireRate()),
                    TextAlignment.LEFT, 0.02f,
                    activeAttachments.isEmpty() ? baseEquipmentY + 0.17f : baseEquipmentY + 0.20f);
        } else {
            gm.renderText(sb, weaponsFont, "WEAPON: " + currentWeaponInfo, TextAlignment.LEFT, 0.02f,
                    baseEquipmentY + 0.08f);

            if (currentWeaponComponents != null && !currentWeaponComponents.isEmpty()) {
                gm.renderText(sb, weaponsFont, "Components: " + currentWeaponComponents, TextAlignment.LEFT, 0.02f,
                        baseEquipmentY + 0.11f);
            }
            if (currentWeaponStats != null && !currentWeaponStats.isEmpty()) {
                gm.renderText(sb, weaponsFont, currentWeaponStats, TextAlignment.LEFT, 0.02f, baseEquipmentY + 0.14f);
            }
        }

        gm.renderText(sb, weaponsFont, "SPIKES: " + spikeCount, TextAlignment.LEFT, 0.02f, baseEquipmentY + 0.20f);
        gm.renderText(
                sb,
                healthFont,
                "1-3: Weapons | 4-8: Attachments | 0: Reset | B: Fire Mode | R: Reload",
                TextAlignment.CENTER,
                0f,
                0.90f);

        gm.renderText(
                sb,
                healthFont,
                "E: Place Spike | U: Undo | L: Logs | SPACE: Shoot",
                TextAlignment.CENTER,
                0f,
                0.95f);

        renderNotifications();
        renderChatMessages();
        sb.end();

        // Render log display if visible
        if (logDisplay != null && logDisplay.isVisible()) {
            logDisplay.render(sb);
        }
    }

    private void renderNotifications() {
        BitmapFont notifFont = notifFontManager.getFontResource().getFont();
        GameManagerFacade gm = GameManagerFacade.getInstance();
        float startY = 0.15f;
        float y = startY;
        for (int i = 0; i < notifications.size(); i++) {
            Notification n = notifications.get(i);
            gm.renderText(sb, notifFont, n.text, TextAlignment.CENTER, 0f, y);
            y += 0.05f;
            if (i >= 3)
                break; // show up to 4
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

        // Bridge Pattern - Handle burst fire
        if (burstShotsRemaining > 0) {
            burstShotTimer -= deltaTime;
            if (burstShotTimer <= 0) {
                shootSingle();
                burstShotsRemaining--;
                burstShotTimer = BURST_DELAY;
            }
        }

        // Bridge Pattern - Handle full auto continuous fire
        if (isShooting && currentFiringMode instanceof FullAutoMechanism) {
            if (currentBridgeWeapon != null && currentBridgeWeapon.getCurrentAmmo() > 0) {
                autoFireTimer -= deltaTime;
                if (autoFireTimer <= 0) {
                    System.out.println("==> FULL AUTO: Firing! Ammo: " + currentBridgeWeapon.getCurrentAmmo());
                    shootSingle();
                    autoFireTimer = AUTO_FIRE_RATE;
                }
            } else {
                // Out of ammo, stop shooting
                System.out.println("==> FULL AUTO: Out of ammo, stopping!");
                isShooting = false;
            }
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
            if (n.ttl <= 0)
                notifications.remove(i);
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

    // Bridge Pattern - Cycle firing mode
    public void cycleFiringMode() {
        if (currentBridgeWeapon == null)
            return;

        // Cycle through modes: Single -> Burst -> Full Auto -> Charged -> Single
        if (currentFiringMode instanceof SingleShotMechanism) {
            currentFiringMode = new BurstFireMechanism();
        } else if (currentFiringMode instanceof BurstFireMechanism) {
            currentFiringMode = new FullAutoMechanism();
        } else if (currentFiringMode instanceof FullAutoMechanism) {
            currentFiringMode = new ChargedShotMechanism();
        } else {
            currentFiringMode = new SingleShotMechanism();
        }

        currentBridgeWeapon.switchFiringMode(currentFiringMode);

        // Show notification
        notifications.add(new Notification(
                "Firing Mode: " + currentFiringMode.getDescription(),
                2.5f));

        System.out.println("==> Switched to: " + currentFiringMode.getDescription());
    }

    // Bridge Pattern - Reload weapon
    public void reloadBridgeWeapon() {
        if (player == null)
            return;
        ReloadMessage m = new ReloadMessage();
        m.setPlayerId(player.getId());
        client.sendTCP(m);
        notifications.add(new Notification("Reloading...", 1.2f));
    }

    // Bridge Pattern - Shoot with firing mechanism behavior
    public void shoot() {
        if (currentBridgeWeapon == null || player == null)
            return;

        // Check ammo
        if (currentBridgeWeapon.getCurrentAmmo() <= 0) {
            System.out.println("Out of ammo! Press R to reload.");
            return;
        }

        // Handle different firing mechanisms
        if (currentFiringMode instanceof SingleShotMechanism) {
            // Single shot - fire once
            shootSingle();
            currentBridgeWeapon.fire(); // Client-side feedback

        } else if (currentFiringMode instanceof BurstFireMechanism) {
            // Burst fire - queue 3 shots
            if (burstShotsRemaining == 0) { // Only start new burst if not already bursting
                BurstFireMechanism burst = (BurstFireMechanism) currentFiringMode;
                burstShotsRemaining = burst.getBurstCount();
                burstShotTimer = 0; // Fire first shot immediately
                currentBridgeWeapon.fire(); // Client-side feedback
            }

        } else if (currentFiringMode instanceof FullAutoMechanism) {
            // Full auto - start continuous fire
            if (!isShooting) {
                isShooting = true;
                autoFireTimer = 0; // Fire immediately
                shootSingle(); // First shot
                currentBridgeWeapon.fire(); // Client-side feedback
                System.out.println("==> FULL AUTO: Started continuous fire! isShooting=" + isShooting);
            }

        } else if (currentFiringMode instanceof ChargedShotMechanism) {
            // Charged shot - start charging
            currentBridgeWeapon.fire(); // This starts charging
        }
    }

    // Helper method to send a single shot to server
    private void shootSingle() {
        if (player == null)
            return;

        ShootMessage m = new ShootMessage();
        m.setPlayerId(player.getId());
        m.setAngleDeg(aimLine.getAngle());
        stats.stats(StatAction.SET, StatType.TOTAL_SHOTS);
        client.sendUDP(m);

        // GameLogEntry shootEvent = new GameLogEntry(
        // System.currentTimeMillis(),
        // "PLAYER_SHOOT",
        // "Player " + player.getId() + " fired at angle " + String.format("%.2f",
        // Math.toDegrees(aimLine.getAngle())) + "°",
        // "DEBUG"
        // );
        // gameLogger.logEvent(shootEvent);
    }

    // Bridge Pattern - Stop shooting (for full auto and charged shot)
    public void stopShooting() {
        if (currentBridgeWeapon == null)
            return;

        if (currentFiringMode instanceof FullAutoMechanism) {
            System.out.println("==> FULL AUTO: Stopped! isShooting was: " + isShooting);
            isShooting = false;
            currentBridgeWeapon.stopFiring();
        } else if (currentFiringMode instanceof ChargedShotMechanism) {
            // Release charged shot
            currentBridgeWeapon.stopFiring();
            shootSingle(); // Fire the charged shot
        }
    }

    // Get bullet size multiplier from decorators
    public float getBulletSizeMultiplier() {
        if (currentBridgeWeapon == null)
            return 1.0f;

        // Check if weapon has decorator-specific bullet size
        if (currentBridgeWeapon instanceof SilencerDecorator) {
            return ((SilencerDecorator) currentBridgeWeapon).getBulletSizeMultiplier();
        } else if (currentBridgeWeapon instanceof DamageBoostDecorator) {
            return ((DamageBoostDecorator) currentBridgeWeapon).getBulletSizeMultiplier();
        }

        return 1.0f; // Default size
    }

    // Get current weapon damage (includes decorator modifications)
    public float getCurrentWeaponDamage() {
        if (currentBridgeWeapon != null) {
            return currentBridgeWeapon.getDamage();
        }
        return 0f;
    }

    private void processInputs() {
        // Block player movement when typing in chat
        if (chatInputActive) {
            return;
        }
        
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
                "INFO");
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
                "Player " + player.getId() + " died. Time alive: " + GameStats.getInstance().getTimeAliveSeconds()
                        + "s",
                "WARN");
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
        powerUps = OMessageParser.getPowerUpsFromGWM(m);

        if (player == null)
            return;

        float oldHealth = player.getHealth();
        players.stream().filter(p -> p.getId() == player.getId())
                .findFirst()
                .ifPresent(p -> {
                    player = p;
                    hasShield = player.hasShield();
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

            if (m.isShouldReload()) {
                reloadBridgeWeapon();
                notifications.add(new Notification("Ammo Refilled!", 2.0f));
            }
        }
    }

    @Override
    public void ammoUpdateReceived(AmmoUpdateMessage m) {
        if (player != null && m.getPlayerId() == player.getId()) {
            if (currentBridgeWeapon != null) {
                // Keep capacity in sync (in case of attachment changes)
                currentBridgeWeapon.setAmmoCapacity(m.getAmmoCapacity());
                // Then set server-authoritative current ammo
                currentBridgeWeapon.setCurrentAmmo(m.getCurrentAmmo());
            }
        }
    }
    
    @Override
    public void chatMessageReceived(ChatMessage m) {
        // Add message to chat history
        chatMessages.add(m);
        
        // Keep only last MAX_CHAT_MESSAGES
        while (chatMessages.size() > MAX_CHAT_MESSAGES) {
            chatMessages.remove(0);
        }
        
        // Log for debugging
        gameLogger.logEvent(new GameLogEntry(
            System.currentTimeMillis(),
            "TEAM_CHAT",
            "[" + m.getTeamName() + "] " + m.getSenderName() + ": " + m.getMessage(),
            "INFO"
        ));
    }
    
    @Override
    public void teamAssignmentReceived(TeamAssignmentMessage m) {
        // Log team assignments
        gameLogger.logEvent(new GameLogEntry(
            System.currentTimeMillis(),
            "TEAM_ASSIGNMENT",
            "Player " + m.getPlayerId() + " (" + m.getPlayerName() + ") joined " + m.getTeamName() + " team",
            "INFO"
        ));
    }
    
    public void setSelectedTeam(String selectedTeam) {
        // If we're already connected (e.g., switching teams), logout first and reinit
        // Also reinit if client is null/closed (e.g., after returning from menu)
        if (player != null || client == null || !client.isConnected()) {
            if (player != null) {
                logout();
            }
            init(); // Reinitialize to create new client connection
        }
        this.selectedTeam = selectedTeam;
        connectToServer();
    }
    
    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void restart() {
        init();
    }
    
    public void logout() {
        if (player != null) {
            LogoutMessage m = new LogoutMessage();
            m.setPlayerId(player.getId());
            client.sendTCP(m);
        }
        if (client != null) {
            client.close();
        }
        player = null;
        chatMessages.clear();
    }

    @Override
    public void dispose() {
        if (player != null) {
            LogoutMessage m = new LogoutMessage();
            m.setPlayerId(player.getId());
            client.sendTCP(m);
        }
        // if (healthFont != null)
        //     healthFont.dispose();
        // if (notifFont != null)
        //     notifFont.dispose();
        healthFontManager.dispose();
        notifFontManager.dispose();
        weaponsFontManager.dispose();
        if (logDisplay != null)
            logDisplay.dispose();
        
        // Dispose chat fonts
        if (chatFont != null) chatFont.dispose();
        if (chatFontRed != null) chatFontRed.dispose();
        if (chatFontBlue != null) chatFontBlue.dispose();
        if (chatFontGreen != null) chatFontGreen.dispose();
        
        // Note: WeaponImageFactory is a singleton and manages its own pool lifecycle
        // Individual weapon images are shared via Flyweight pattern
        
        stats.endSession();

        sc.getAchievementManager().removeListener(this);
    }

    @Override
    public void onAchievementUnlocked(Achievement achievement) {
        notifications.add(new Notification("Achievement Unlocked: " + achievement.getTitle(), 3.0f));
    }
    
    /**
     * Render chat messages in the top-right corner
     */
    private void renderChatMessages() {
        if (chatMessages.isEmpty() && !chatInputActive) return;
        
        GameManagerFacade gm = GameManagerFacade.getInstance();
        
        float startX = 0.65f; // Right side of screen
        float startY = 0.05f; // Top
        
        // Render chat messages using pre-created fonts
        for (int i = 0; i < chatMessages.size(); i++) {
            ChatMessage msg = chatMessages.get(i);
            BitmapFont coloredFont = getTeamFont(msg.getTeamName());
            
            String displayText = msg.getSenderName() + ": " + msg.getMessage();
            gm.renderText(sb, coloredFont, displayText, TextAlignment.LEFT, startX, startY + (i * 0.03f));
        }
        
        // Render chat input if active
        if (chatInputActive) {
            float inputY = startY + (chatMessages.size() * 0.03f) + 0.02f;
            String inputDisplay = "> " + chatInputText.toString() + "_";
            gm.renderText(sb, chatFont, inputDisplay, TextAlignment.LEFT, startX, inputY);
            gm.renderText(sb, chatFont, "(Enter to send, Esc to cancel)", TextAlignment.LEFT, startX, inputY + 0.03f);
        } else {
            // Show hint to open chat
            gm.renderText(sb, chatFont, "T: Team Chat", TextAlignment.LEFT, startX, startY + (chatMessages.size() * 0.03f) + 0.02f);
        }
    }
    
    private BitmapFont getTeamFont(String teamName) {
        return switch (teamName) {
            case "RED" -> chatFontRed;
            case "BLUE" -> chatFontBlue;
            case "GREEN" -> chatFontGreen;
            default -> chatFont;
        };
    }
    
    /**
     * Toggle chat input mode
     */
    public void toggleChatInput() {
        chatInputActive = !chatInputActive;
        if (!chatInputActive) {
            chatInputText.setLength(0); // Clear input
            justOpenedChat = false;
        } else {
            justOpenedChat = true; // Set flag when opening chat
        }
    }
    
    /**
     * Add character to chat input
     */
    public void addChatCharacter(char character) {
        if (chatInputActive && chatInputText.length() < 50) {
            chatInputText.append(character);
        }
    }
    
    /**
     * Check if we just opened chat (to filter the trigger key)
     */
    public boolean isJustOpenedChat() {
        return justOpenedChat;
    }
    
    /**
     * Clear the just opened chat flag
     */
    public void clearJustOpenedChat() {
        justOpenedChat = false;
    }
    
    /**
     * Remove last character from chat input
     */
    public void removeChatCharacter() {
        if (chatInputActive && chatInputText.length() > 0) {
            chatInputText.setLength(chatInputText.length() - 1);
        }
    }
    
    /**
     * Send chat message to team
     */
    public void sendChatMessage() {
        if (chatInputActive && chatInputText.length() > 0 && player != null) {
            ChatMessage msg = new ChatMessage();
            msg.setSenderId(player.getId());
            msg.setSenderName("Player" + player.getId());
            msg.setMessage(chatInputText.toString());
            msg.setTeamName(selectedTeam != null ? selectedTeam : "RED");
            msg.setTimestamp(System.currentTimeMillis());
            
            client.sendUDP(msg);
            
            chatInputText.setLength(0);
            chatInputActive = false;
        }
    }
    
    public boolean isChatInputActive() {
        return chatInputActive;
    }
    
}
