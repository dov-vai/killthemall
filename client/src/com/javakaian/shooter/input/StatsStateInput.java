package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.shooter.command.*;
import com.javakaian.states.StatsState;

public class StatsStateInput extends InputAdapter {

	private final StatsState statsState;
	private final KeyBindingManager keyBindingManager;

	public StatsStateInput(StatsState statsState) {
		this.statsState = statsState;
		this.keyBindingManager = new KeyBindingManager();
		setupDefaultKeyBindings();
	}
	
	/**
	 * Setup default key bindings for StatsState
	 */
	private void setupDefaultKeyBindings() {
		keyBindingManager.bindKey(Keys.R, new ResetStatsCommand(statsState));
		keyBindingManager.bindKey(Keys.ESCAPE, new StatsToMenuCommand(statsState));
	}

	@Override
	public boolean keyDown(int keycode) {
		return keyBindingManager.handleKeyPress(keycode);
	}
	
	/**
	 * Get the key binding manager for runtime key reconfiguration
	 */
	public KeyBindingManager getKeyBindingManager() {
		return keyBindingManager;
	}
}


