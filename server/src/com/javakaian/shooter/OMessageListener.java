package com.javakaian.shooter;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.LoginMessage;
import com.javakaian.network.messages.LogoutMessage;
import com.javakaian.network.messages.PositionMessage;
import com.javakaian.network.messages.ShootMessage;
import com.javakaian.network.messages.WeaponChangeMessage;
import com.javakaian.network.messages.PlaceSpikeMessage;
import com.javakaian.network.messages.UndoSpikeMessage;

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

}
