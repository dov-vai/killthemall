package com.javakaian.shooter.utils.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.javakaian.shooter.utils.stats.messages.*;

public final class GameStats extends StatsObservable{

	private static final String PREF_NAME = "killthemall_stats";
	private static final String KEY_TOTAL_SESSIONS = "total_sessions";
	private static final String KEY_TOTAL_DEATHS = "total_deaths";
	private static final String KEY_TOTAL_SHOTS = "total_shots";
	private static final String KEY_TOTAL_DAMAGE = "total_damage";
	private static final String KEY_TOTAL_DISTANCE = "total_distance";
	private static final String KEY_BEST_TIME = "best_time";

	private static final GameStats INSTANCE = new GameStats();

	private Preferences prefs;

	private long sessionStartNanos;
	private long sessionEndNanos;
	private int sessionShotsFired;
	private float sessionDamageTaken;
	private float sessionDistanceTraveled;
	private boolean sessionActive;

	private int totalSessions;
	private int totalDeaths;
	private int totalShots;
	private float totalDamage;
	private float totalDistance;
	private float bestTimeSeconds;

	private GameStats() {
		prefs = Gdx.app != null ? Gdx.app.getPreferences(PREF_NAME) : null;
		load();
	}

	public static GameStats getInstance() {
		return INSTANCE;
	}

    @Override
    public synchronized void addListener(StatsObserver listener) {
        super.addListener(listener);
    }

    @Override
    public synchronized void removeListener(StatsObserver listener) {
        super.removeListener(listener);
    }

	public synchronized void load() {
		if (prefs == null && Gdx.app != null) {
			prefs = Gdx.app.getPreferences(PREF_NAME);
		}
		if (prefs == null) return;
		totalSessions = prefs.getInteger(KEY_TOTAL_SESSIONS, 0);
		totalDeaths = prefs.getInteger(KEY_TOTAL_DEATHS, 0);
		totalShots = prefs.getInteger(KEY_TOTAL_SHOTS, 0);
		totalDamage = prefs.getFloat(KEY_TOTAL_DAMAGE, 0f);
		totalDistance = prefs.getFloat(KEY_TOTAL_DISTANCE, 0f);
		bestTimeSeconds = prefs.getFloat(KEY_BEST_TIME, 0f);
	}

	public synchronized void save() {
		if (prefs == null) return;
		prefs.putInteger(KEY_TOTAL_SESSIONS, totalSessions);
		prefs.putInteger(KEY_TOTAL_DEATHS, totalDeaths);
		prefs.putInteger(KEY_TOTAL_SHOTS, totalShots);
		prefs.putFloat(KEY_TOTAL_DAMAGE, totalDamage);
		prefs.putFloat(KEY_TOTAL_DISTANCE, totalDistance);
		prefs.putFloat(KEY_BEST_TIME, bestTimeSeconds);
		prefs.flush();
	}

	public synchronized void resetSession() {
		sessionShotsFired = 0;
		sessionDamageTaken = 0f;
		sessionDistanceTraveled = 0f;
		sessionStartNanos = 0L;
		sessionEndNanos = 0L;
		sessionActive = false;
	}

	public synchronized void startSession() {
		resetSession();
		sessionStartNanos = System.nanoTime();
		sessionActive = true;
		totalSessions++;
		notify(new SessionStartedMessage(totalSessions));
	}

	public synchronized void endSession() {
		if (!sessionActive) return;
		sessionEndNanos = System.nanoTime();
		sessionActive = false;
		totalShots += sessionShotsFired;
		totalDamage += sessionDamageTaken;
		totalDistance += sessionDistanceTraveled;
		float time = getTimeAliveSecondsUnsafe();
		boolean bestUpdated = false;
		if (time > bestTimeSeconds) {
			bestTimeSeconds = time;
			bestUpdated = true;
		}
        save();
		notify(new SessionEndedMessage(time, bestUpdated, sessionShotsFired, sessionDamageTaken, sessionDistanceTraveled));
		notify(new TotalsChangedMessage(totalSessions, totalDeaths, totalShots, totalDamage, totalDistance, bestTimeSeconds));
	}

	public synchronized void incrementDeaths() {
		totalDeaths++;
		notify(new DeathsChangedMessage(totalDeaths));
	}

	public synchronized void incrementShotsFired() {
		sessionShotsFired++;
		notify(new ShotsFiredChangedMessage(sessionShotsFired, totalShots + sessionShotsFired));
	}

	public synchronized void addDamageTaken(float amount) {
		if (amount <= 0) return;
		sessionDamageTaken += amount;
		notify(new DamageTakenChangedMessage(sessionDamageTaken, totalDamage + sessionDamageTaken));
	}

	public synchronized void addDistanceTraveled(float amount) {
		if (amount <= 0) return;
		sessionDistanceTraveled += amount;
		notify(new DistanceTraveledChangedMessage(sessionDistanceTraveled, totalDistance + sessionDistanceTraveled));
	}

	public synchronized float getTimeAliveSeconds() {
		return getTimeAliveSecondsUnsafe();
	}

	private float getTimeAliveSecondsUnsafe() {
		long end = sessionEndNanos == 0L ? System.nanoTime() : sessionEndNanos;
		if (sessionStartNanos == 0L) return 0f;
		return (end - sessionStartNanos) / 1_000_000_000f;
	}

	public synchronized int getTotalSessions() {  return totalSessions; }
	public synchronized int getTotalDeaths() {  return totalDeaths; }
	public synchronized int getTotalShots() { return totalShots; }
	public synchronized float getTotalDamage() { return totalDamage;}
	public synchronized float getTotalDistance() { return totalDistance; }
	public synchronized float getBestTimeSeconds() { return bestTimeSeconds; }

	public synchronized void resetTotals() {
		totalSessions = 0;
		totalDeaths = 0;
		totalShots = 0;
		totalDamage = 0f;
		totalDistance = 0f;
		bestTimeSeconds = 0f;
		save();
		notify(new TotalsResetMessage());
	}
}


