package com.javakaian.shooter;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.LoginMessage;
import com.javakaian.network.messages.LogoutMessage;
import com.javakaian.network.messages.PositionMessage;
import com.javakaian.network.messages.ShootMessage;

public interface OMessageListener {

	/**
	 * This should be received with playerID and bullet direction
	 * 
	 * @param m
	 */
    void shootReceived(ShootMessage m);

	/**
	 * PlayerID, and location should be received.
	 */
    void loginReceived(Connection con, LoginMessage m);

	/**
	 * PlayerID should be received.
	 */
    void logoutReceived(LogoutMessage m);

	/**
	 * PlayerID and direction should be received.
	 * 
	 * @param m
	 */
    void playerMovedReceived(PositionMessage m);

}
