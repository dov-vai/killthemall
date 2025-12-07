package com.javakaian.network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.javakaian.network.messages.*;
import com.javakaian.shooter.OMessageListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;

public class OClient {

    private Client client;
    private OMessageListener game;

    private String inetAddress;

    private Logger logger = Logger.getLogger(OClient.class);

    public OClient(String inetAddress, OMessageListener game) {

        this.game = game;
        this.inetAddress = inetAddress;
        client = new Client();
        registerClasses();
        addListeners();

    }

    public void connect() {
        client.start();
        try {
            logger.debug("Attempting to connect args[0]: " + inetAddress);
            client.connect(5000, InetAddress.getByName(inetAddress), 1234, 1235);
        } catch (IOException e) {
            logger.log(Level.ALL, e);
        }
    }

    private void addListeners() {

        client.addListener(new Listener() {

            @Override
            public void received(Connection connection, Object object) {

                Gdx.app.postRunnable(() -> {

                    if (object instanceof LoginMessage m) {
                        OClient.this.game.loginReceived(m);

                    } else if (object instanceof LogoutMessage m) {
                        OClient.this.game.logoutReceived(m);
                    } else if (object instanceof GameWorldMessage m) {

                        OClient.this.game.gwmReceived(m);
                    } else if (object instanceof PlayerDiedMessage m) {

                        OClient.this.game.playerDiedReceived(m);
                    } else if (object instanceof WeaponInfoMessage m) {
                        OClient.this.game.weaponInfoReceived(m);
                    } else if (object instanceof AmmoUpdateMessage m) {
                        OClient.this.game.ammoUpdateReceived(m);
                    } else if (object instanceof InventoryUpdateMessage m) {
                        OClient.this.game.inventoryUpdateReceived(m);
                    } else if (object instanceof ChatMessage m) {
                        OClient.this.game.chatMessageReceived(m);
                    } else if (object instanceof TeamAssignmentMessage m) {
                        OClient.this.game.teamAssignmentReceived(m);
                    }

                });

            }

        });
    }

    /**
     * This function register every class that will be sent back and forth between
     * client and server.
     */
    private void registerClasses() {
        // messages
        this.client.getKryo().register(LoginMessage.class);
        this.client.getKryo().register(LogoutMessage.class);
        this.client.getKryo().register(GameWorldMessage.class);
        this.client.getKryo().register(PositionMessage.class);
        this.client.getKryo().register(PositionMessage.Direction.class);
        this.client.getKryo().register(ShootMessage.class);
        this.client.getKryo().register(PlayerDiedMessage.class);

        this.client.getKryo().register(WeaponChangeMessage.class);
        this.client.getKryo().register(WeaponInfoMessage.class);
        this.client.getKryo().register(AmmoUpdateMessage.class);
        this.client.getKryo().register(ReloadMessage.class);

        this.client.getKryo().register(PlaceSpikeMessage.class);
        this.client.getKryo().register(UndoSpikeMessage.class);
        this.client.getKryo().register(InventoryUpdateMessage.class);
        
        // Team chat messages - Mediator pattern
        this.client.getKryo().register(ChatMessage.class);
        this.client.getKryo().register(TeamAssignmentMessage.class);

        // primitive arrays
        this.client.getKryo().register(float[].class);

    }

    public void close() {
        client.close();
    }

    public void sendTCP(Object m) {
        client.sendTCP(m);
    }

    public void sendUDP(Object m) {
        client.sendUDP(m);
    }

}
