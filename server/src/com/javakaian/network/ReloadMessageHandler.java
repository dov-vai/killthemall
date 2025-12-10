package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.ReloadMessage;
import com.javakaian.shooter.OMessageListener;

public class ReloadMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof ReloadMessage m) {
            listener.reloadReceived(m);
            return true;
        }
        return false;
    }
}
