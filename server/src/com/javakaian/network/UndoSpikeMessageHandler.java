package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.UndoSpikeMessage;
import com.javakaian.shooter.OMessageListener;

public class UndoSpikeMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof UndoSpikeMessage m) {
            listener.undoSpikeReceived(m);
            return true;
        }
        return false;
    }
}
