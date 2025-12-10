package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.javakaian.shooter.OMessageListener;

public abstract class MessageHandler {
    protected MessageHandler next;

    public void setNext(MessageHandler next) {
        this.next = next;
    }

    public void handle(Connection con, Object message, OMessageListener listener) {
        if (!process(con, message, listener) && next != null) {
            next.handle(con, message, listener);
        }
    }

    protected abstract boolean process(Connection con, Object message, OMessageListener listener);
}
