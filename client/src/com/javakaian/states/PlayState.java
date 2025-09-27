package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.javakaian.network.OClient;
import com.javakaian.network.messages.*;
import com.javakaian.network.messages.PositionMessage.Direction;
import com.javakaian.shooter.OMessageListener;
import com.javakaian.shooter.input.PlayStateInput;
import com.javakaian.shooter.shapes.AimLine;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.utils.GameConstants;
import com.javakaian.shooter.utils.GameUtils;
import com.javakaian.shooter.utils.OMessageParser;
import com.javakaian.shooter.utils.GameStats;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the state where gameplay happens.
 *
 * @author oguz
 */
public class PlayState extends State implements OMessageListener {

    private Player player;
    private List<Player> players;
    private List<Enemy> enemies;
    private List<Bullet> bullets;
    private AimLine aimLine;

    private OClient client;

    private BitmapFont healthFont;

    private float lastX, lastY;

    public PlayState(StateController sc) {
        super(sc);

        init();
        ip = new PlayStateInput(this);
        healthFont = GameUtils.generateBitmapFont(20, Color.WHITE);
    }

    private void init() {

        client = new OClient(sc.getInetAddress(), this);
        client.connect();

        players = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        aimLine = new AimLine(new Vector2(0, 0), new Vector2(0, 0));
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
        if (player == null)
            return;

        ScreenUtils.clear(0, 0, 0, 1);

        sr.begin(ShapeType.Line);
        sr.setColor(Color.RED);
        players.forEach(p -> p.render(sr));
        sr.setColor(Color.WHITE);
        enemies.forEach(e -> e.render(sr));
        bullets.forEach(b -> b.render(sr));
        sr.setColor(Color.BLUE);
        player.render(sr);
        sr.setColor(Color.WHITE);
        aimLine.render(sr);
        followPlayer();
        sr.end();

        sb.begin();
        GameUtils.renderCenter("HEALTH: " + player.getHealth(), sb, healthFont, 0.1f);
        sb.end();

    }

    /**
     * This function let camera to follow player smootly.
     */
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
    }

    public void scrolled(float amountY) {
        if (amountY > 0) {
            camera.zoom += 0.2F;
        } else {
            if (camera.zoom >= 0.4) {
                camera.zoom -= 0.2F;
            }
        }
    }

    /**
     * This should be called when player shoot a bullet. It sends a shoot message to
     * the server with angle value.
     */
    public void shoot() {

        ShootMessage m = new ShootMessage();
        m.setPlayerId(player.getId());
        m.setAngleDeg(aimLine.getAngle());
        GameStats.getInstance().incrementShotsFired();
        client.sendUDP(m);

    }

    /**
     * Whenever player press a button, this creates necessary position message and
     * sends it to the server.
     */
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

        enemies = OMessageParser.getEnemiesFromGWM(m);
        bullets = OMessageParser.getBulletsFromGWM(m);

        players = OMessageParser.getPlayersFromGWM(m);

        if (player == null)
            return;
        // Find yourself.
        float oldHealth = player.getHealth();
        players.stream().filter(p -> p.getId() == player.getId()).findFirst().ifPresent(p -> {
            player = p;
            float newHealth = player.getHealth();
            if (newHealth < oldHealth) {
                GameStats.getInstance().addDamageTaken(oldHealth - newHealth);
            }
        });
        // Remove yourself from playerlist.
        players.removeIf(p -> p.getId() == player.getId());

    }

    public void restart() {
        init();
    }

    @Override
    public void dispose() {

        LogoutMessage m = new LogoutMessage();
        m.setPlayerId(player.getId());
        client.sendTCP(m);
    }

}
