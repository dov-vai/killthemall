package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.LogoutMessage;
import com.javakaian.shooter.OMessageListener;

public class LogoutMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof LogoutMessage m) {
            listener.logoutReceived(m);
            return true;
        }
        return false;
    }
}
