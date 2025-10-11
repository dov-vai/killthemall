package com.javakaian.shooter.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.javakaian.shooter.utils.GameStats;
import com.javakaian.shooter.utils.GameStatsListener;

import java.util.*;

public class AchievementManager implements GameStatsListener {

    private static final String PREF_NAME = "killthemall_achievements";
    private static final String KEY_UNLOCKED = "unlocked_ids";

    private final Map<String, Achievement> catalog = new LinkedHashMap<>();
    private final Set<String> unlocked = new LinkedHashSet<>();
    private final List<AchievementListener> listeners = new ArrayList<>();

    private Preferences prefs;

    public AchievementManager() {
        prefs = Gdx.app != null ? Gdx.app.getPreferences(PREF_NAME) : null;
        bootstrapCatalog();
        load();
        GameStats.getInstance().addListener(this);
    }

    private void bootstrapCatalog() {
        add(new Achievement("first_blood", "First Blood", "Die for the first time"));
        add(new Achievement("rookie_shooter", "Rookie Shooter", "Fire 100 shots in total"));
        add(new Achievement("marathon", "Marathon", "Travel 5000 units in total"));
        add(new Achievement("iron_man", "Iron Man", "Survive 60 seconds in a session"));
    }

    private void add(Achievement a) { catalog.put(a.getId(), a); }

    public Collection<Achievement> getCatalog() { return catalog.values(); }

    public List<Achievement> getUnlocked() {
        List<Achievement> list = new ArrayList<>();
        for (String id : unlocked) {
            Achievement a = catalog.get(id);
            if (a != null) list.add(a);
        }
        return list;
    }

    public boolean isUnlocked(String id) { return unlocked.contains(id); }

    public void addListener(AchievementListener l) {
        if (l != null && !listeners.contains(l)) listeners.add(l);
    }

    public void removeListener(AchievementListener l) { listeners.remove(l); }

    private void unlock(String id) {
        if (unlocked.contains(id)) return;
        unlocked.add(id);
        save();
        Achievement a = catalog.get(id);
        if (a != null) {
            for (var l : listeners) {
                try { l.onAchievementUnlocked(a); } catch (Exception ignored) {}
            }
        }
    }

    private void load() {
        if (prefs == null && Gdx.app != null) prefs = Gdx.app.getPreferences(PREF_NAME);
        if (prefs == null) return;
        String csv = prefs.getString(KEY_UNLOCKED, "");
        if (!csv.isEmpty()) {
            unlocked.addAll(Arrays.asList(csv.split(",")));
        }
    }

    private void save() {
        if (prefs == null) return;
        String csv = String.join(",", unlocked);
        prefs.putString(KEY_UNLOCKED, csv);
        prefs.flush();
    }

    @Override
    public void onDeathsChanged(int totalDeaths) {
        if (totalDeaths >= 1) unlock("first_blood");
    }

    @Override
    public void onShotsFiredChanged(int sessionShots, int projectedTotalShots) {
        if (projectedTotalShots >= 100) unlock("rookie_shooter");
    }

    @Override
    public void onDistanceTraveledChanged(float sessionDistance, float projectedTotalDistance) {
        if (projectedTotalDistance >= 5000f) unlock("marathon");
    }

    @Override
    public void onSessionEnded(float sessionTimeSeconds, boolean bestTimeUpdated, int sessionShots, float sessionDamage, float sessionDistance) {
        if (sessionTimeSeconds >= 60f) unlock("iron_man");
    }
}
