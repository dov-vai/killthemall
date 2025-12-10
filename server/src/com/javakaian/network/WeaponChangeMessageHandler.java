package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.WeaponChangeMessage;
import com.javakaian.shooter.OMessageListener;

public class WeaponChangeMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof WeaponChangeMessage m) {
            listener.weaponChangeReceived(m);
            return true;
        }
        return false;
    }
}
