package com.javakaian.shooter.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.javakaian.states.StatsState;

public class StatsStateInput extends InputAdapter {

	private final StatsState statsState;

	public StatsStateInput(StatsState statsState) {
		this.statsState = statsState;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Keys.R:
				statsState.resetStats();
				break;
			case Keys.ESCAPE:
				statsState.backToMenu();
				break;
			default:
				break;
		}
		return true;
	}
}


