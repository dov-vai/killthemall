package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.input.StatsStateInput;
import com.javakaian.shooter.utils.GameStats;
import com.javakaian.shooter.utils.GameUtils;

public class StatsState extends State {

	private BitmapFont smallFont;

	public StatsState(StateController sc) {
		super(sc);
		smallFont = GameUtils.generateBitmapFont(28, Color.WHITE);
		ip = new StatsStateInput(this);
	}

	@Override
	public void render() {

		float red = 30f;
		float green = 40f;
		float blue = 55f;
		Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		GameUtils.renderCenter("Statistics", sb, bitmapFont, 0.2f);
		var s = GameStats.getInstance();
		GameUtils.renderCenter("Total Sessions: " + s.getTotalSessions(), sb, smallFont, 0.35f);
		GameUtils.renderCenter("Best Time: " + String.format("%.1fs", s.getBestTimeSeconds()), sb, smallFont, 0.42f);
		GameUtils.renderCenter("Total Deaths: " + s.getTotalDeaths(), sb, smallFont, 0.49f);
		GameUtils.renderCenter("Total Shots: " + s.getTotalShots(), sb, smallFont, 0.56f);
		GameUtils.renderCenter("Total Damage: " + String.format("%.0f", s.getTotalDamage()), sb, smallFont, 0.63f);
		GameUtils.renderCenter("Total Distance: " + String.format("%.0f", s.getTotalDistance()), sb, smallFont, 0.70f);
		GameUtils.renderCenter("R - Reset Stats    ESC - Back", sb, smallFont, 0.82f);
		sb.end();
	}

	@Override
	public void update(float deltaTime) {
		// no-op
	}

	@Override
	public void dispose() {
		// no-op
	}

	public void resetStats() {
		GameStats.getInstance().resetTotals();
	}

	public void backToMenu() {
		sc.setState(StateEnum.MENU_STATE);
	}
}


