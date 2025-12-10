package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.RewindMessage;
import com.javakaian.shooter.OMessageListener;

public class RewindMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof RewindMessage m) {
            listener.rewindReceived(m);
            return true;
        }
        return false;
    }
}