package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.ShootMessage;
import com.javakaian.shooter.OMessageListener;

public class ShootMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof ShootMessage m) {
            listener.shootReceived(m);
            return true;
        }
        return false;
    }
}
