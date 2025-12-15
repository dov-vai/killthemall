package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.PlaceSpikeMessage;
import com.javakaian.shooter.OMessageListener;

public class PlaceSpikeMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof PlaceSpikeMessage m) {
            listener.placeSpikeReceived(m);
//            return true;
        }
        return false;
    }
}
