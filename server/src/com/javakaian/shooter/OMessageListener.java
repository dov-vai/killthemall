package com.javakaian.shooter;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.*;

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

    /**
     * PlayerID and weapon configuration should be received.
     */
    void weaponChangeReceived(WeaponChangeMessage m);

    /**
     * Player wants to place a spike
     */
    void placeSpikeReceived(PlaceSpikeMessage m);

    /**
     * Player wants to undo their last spike placement
     */
    void undoSpikeReceived(UndoSpikeMessage m);

    /**
     * Player requests a reload of their current weapon
     */
    void reloadReceived(ReloadMessage m);

    /**
     * Player wants to use a power-up from their inventory
     */
    void usePowerUpReceived(UsePowerUpMessage m);

    /**
     * Invoked when a network connection is closed unexpectedly or normally.
     * Implementations should clean up any player state associated with this connection.
     */
    void disconnected(Connection con);

}
