package com.javakaian.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.javakaian.shooter.input.StatsStateInput;
import com.javakaian.shooter.utils.GameManagerFacade;
import com.javakaian.shooter.utils.Subsystems.StatAction;
import com.javakaian.shooter.utils.Subsystems.StatType;
import com.javakaian.shooter.utils.Subsystems.TextAlignment;
import com.javakaian.shooter.utils.stats.GameStats;
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
		float red = 30f, green = 40f, blue = 55f;
		Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GameManagerFacade gm = GameManagerFacade.getInstance();

		sb.begin();

		gm.renderText(sb, bitmapFont, "Statistics", TextAlignment.CENTER, 0f, 0.2f);

		float startY = 0.35f;
		float stepY = 0.07f;

		gm.renderText(sb, smallFont, "Total Sessions: " +
				(int) gm.stats(StatAction.GET, StatType.TOTAL_SESSIONS), TextAlignment.CENTER, 0f, startY);

		gm.renderText(sb, smallFont, "Best Time: " +
				String.format("%.1fs", gm.stats(StatAction.GET, StatType.BEST_TIME)), TextAlignment.CENTER, 0f, startY + stepY);

		gm.renderText(sb, smallFont, "Total Deaths: " +
				(int) gm.stats(StatAction.GET, StatType.TOTAL_DEATHS), TextAlignment.CENTER, 0f, startY + stepY * 2);

		gm.renderText(sb, smallFont, "Total Shots: " +
				(int) gm.stats(StatAction.GET, StatType.TOTAL_SHOTS), TextAlignment.CENTER, 0f, startY + stepY * 3);

		gm.renderText(sb, smallFont, "Total Damage: " +
				String.format("%.0f", gm.stats(StatAction.GET, StatType.TOTAL_DAMAGE)), TextAlignment.CENTER, 0f, startY + stepY * 4);

		gm.renderText(sb, smallFont, "Total Distance: " +
				String.format("%.0f", gm.stats(StatAction.GET, StatType.TOTAL_DISTANCE)), TextAlignment.CENTER, 0f, startY + stepY * 5);

		gm.renderText(sb, smallFont, "R - Reset Stats    ESC - Back", TextAlignment.CENTER, 0f, 0.82f);

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


