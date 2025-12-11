package com.javakaian.shooter.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.javakaian.shooter.utils.stats.GameStats;
import com.javakaian.shooter.utils.stats.StatsObserver;
import com.javakaian.shooter.utils.stats.messages.*;

import java.util.*;

public class AchievementManager extends AchievementsObservable implements StatsObserver {

    private static final String PREF_NAME = "killthemall_achievements";
    private static final String KEY_UNLOCKED = "unlocked_ids";

    private final Map<String, Achievement> catalog = new LinkedHashMap<>();
    private final Set<String> unlocked = new LinkedHashSet<>();

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

    private void add(Achievement a) {
        catalog.put(a.getId(), a);
    }

    public Collection<Achievement> getCatalog() {
        return catalog.values();
    }

    public List<Achievement> getUnlocked() {
        List<Achievement> list = new ArrayList<>();
        for (String id : unlocked) {
            Achievement a = catalog.get(id);
            if (a != null) list.add(a);
        }
        return list;
    }

    public boolean isUnlocked(String id) {
        return unlocked.contains(id);
    }
    
    /**
     * Iterator Pattern - Create iterator for all achievements
     * @return Iterator for all achievements in catalog
     */
    public AchievementIterator createIterator() {
        return new ConcreteAchievementIterator(new ArrayList<>(catalog.values()));
    }
    
    /**
     * Iterator Pattern - Create iterator for unlocked achievements only
     * @return Iterator for unlocked achievements
     */
    public AchievementIterator createUnlockedIterator() {
        return new FilteredAchievementIterator(
            new ArrayList<>(catalog.values()),
            achievement -> unlocked.contains(achievement.getId())
        );
    }
    
    /**
     * Iterator Pattern - Create iterator for locked achievements only
     * @return Iterator for locked achievements
     */
    public AchievementIterator createLockedIterator() {
        return new FilteredAchievementIterator(
            new ArrayList<>(catalog.values()),
            achievement -> !unlocked.contains(achievement.getId())
        );
    }

    private void unlock(String id) {
        if (unlocked.contains(id)) return;
        unlocked.add(id);
        save();
        Achievement a = catalog.get(id);
        notify(a);
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
    public void onStatsReceived(StatsMessage message) {
        switch (message) {
            case DeathsChangedMessage m when m.totalDeaths() >= 1 -> unlock("first_blood");
            case ShotsFiredChangedMessage m when m.projectedTotalShots() >= 100 -> unlock("rookie_shooter");
            case DistanceTraveledChangedMessage m when m.projectedTotalDistance() >= 5000f -> unlock("marathon");
            case SessionEndedMessage m when m.sessionTimeSeconds() >= 60f -> unlock("iron_man");
            default -> {
            }
        }
    }
}
