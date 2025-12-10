package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.network.messages.ChatMessage;
import com.javakaian.shooter.OMessageListener;

public class ChatMessageHandler extends MessageHandler {
    @Override
    protected boolean process(Connection con, Object message, OMessageListener listener) {
        if (message instanceof ChatMessage m) {
            listener.chatMessageReceived(m);
            return true;
        }
        return false;
    }
}
