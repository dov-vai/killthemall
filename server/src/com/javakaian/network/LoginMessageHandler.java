package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.LoginMessage;
import com.javakaian.shooter.OMessageListener;

public class LoginMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof LoginMessage m) {
            listener.loginReceived(con, m);
//            return true;
        }
        return false;
    }
}
