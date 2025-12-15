package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.LogoutMessage;
import com.javakaian.shooter.OMessageListener;

public class MessageHandlerLogger extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        Log(message);
        return false;
    }

    private void Log(Object message){
        System.out.println("MESSAGE FROM LOGGER:" + message.toString());
    }
}
