package com.javakaian.shooter.utils;

import com.javakaian.network.messages.GameWorldMessage;
import com.javakaian.shooter.shapes.Bullet;
import com.javakaian.shooter.shapes.Enemy;
import com.javakaian.shooter.shapes.Player;
import com.javakaian.shooter.shapes.Spike;
import com.javakaian.shooter.shapes.PlacedSpike;

import java.util.ArrayList;
import java.util.List;

/**
 * A parsers class which has methods to constructs objects like
 * Players,Enemy,Bulltes from GameWorldMessage.
 *
 * @author oguz
 */
public class OMessageParser {

    private OMessageParser() {
    }

    /**
     * Returns a enemy list from gameworld message.
     */
    public static List<Enemy> getEnemiesFromGWM(GameWorldMessage m) {

        float[] temp = m.getEnemies();
        List<Enemy> elist = new ArrayList<>();
        for (int i = 0; i < temp.length / 2; i++) {

            float x = temp[i * 2];
            float y = temp[i * 2 + 1];

            Enemy e = new Enemy(x, y, 10);
            elist.add(e);

        }
        return elist;

    }

    /**
     * Returns a player list from gameworld message. Including clients itself. Thats
     * why when clients are rendering players on their screen, they should exclude
     * the player with the same id.
     */
    public static List<Player> getPlayersFromGWM(GameWorldMessage m) {

        float[] tp = m.getPlayers();
        List<Player> plist = new ArrayList<>();
        for (int i = 0; i < tp.length / 4; i++) {

            float x = tp[i * 4];
            float y = tp[i * 4 + 1];
            float id = tp[i * 4 + 2];
            float health = tp[i * 4 + 3];
            Player p = new Player(x, y, 50);
            p.setHealth((int) health);
            p.setId((int) id);

            plist.add(p);

        }
        return plist;

    }

    /**
     * Returns a bullet list from gameworld message.
     */
    public static List<Bullet> getBulletsFromGWM(GameWorldMessage m) {

        float[] tb = m.getBullets();

        List<Bullet> blist = new ArrayList<>();
        for (int i = 0; i < tb.length / 3; i++) {
            float x = tb[i * 3];
            float y = tb[i * 3 + 1];
            float size = tb[i * 3 + 2];

            Bullet b = new Bullet(x, y, size);

            blist.add(b);
        }

        return blist;
    }
    
    /**
     * Returns a spike pickup list from gameworld message.
     */
    public static List<Spike> getSpikesFromGWM(GameWorldMessage m) {
        float[] ts = m.getSpikes();
        if (ts == null) return new ArrayList<>();
        
        List<Spike> slist = new ArrayList<>();
        for (int i = 0; i < ts.length / 3; i++) {
            float x = ts[i * 3];
            float y = ts[i * 3 + 1];
            float size = ts[i * 3 + 2];
            
            Spike s = new Spike(x, y, size);
            slist.add(s);
        }
        
        return slist;
    }
    
    /**
     * Returns a placed spike list from gameworld message.
     */
    public static List<PlacedSpike> getPlacedSpikesFromGWM(GameWorldMessage m) {
        float[] tps = m.getPlacedSpikes();
        if (tps == null) return new ArrayList<>();
        
        List<PlacedSpike> pslist = new ArrayList<>();
        for (int i = 0; i < tps.length / 4; i++) {
            float x = tps[i * 4];
            float y = tps[i * 4 + 1];
            float size = tps[i * 4 + 2];
            float rotation = tps[i * 4 + 3];
            
            PlacedSpike ps = new PlacedSpike(x, y, size, rotation);
            pslist.add(ps);
        }
        
        return pslist;
    }

}
