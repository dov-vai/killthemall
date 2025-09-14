package com.javakaian.network.messages;

/***
 * Whenever player press a button from a keyboard this message should be send.
 */
public class PositionMessage {

	/** Player ID */
	private int playerId;
	/**
	 * The direction that player wants to move. Server will check the direction and
	 * let player move if its possible.
	 */
	private DIRECTION direction;

	public enum DIRECTION {
		LEFT, RIGHT, DOWN, UP
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public DIRECTION getDirection() {
		return direction;
	}

	public void setDirection(DIRECTION direction) {
		this.direction = direction;
	}

}
