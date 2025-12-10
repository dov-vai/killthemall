package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.PositionMessage;
import com.javakaian.shooter.OMessageListener;

public class PositionMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof PositionMessage m) {
            listener.playerMovedReceived(m);
            return true;
        }
        return false;
    }
}
