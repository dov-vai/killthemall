package com.javakaian.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.javakaian.network.messages.*;
import com.javakaian.shooter.OMessageListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author oguz
 * <p>
 * Server object which is responsible for creating kryo server and
 * managing it.
 * <p>
 * Every message received by server queued by this object and get
 * processed 60 time per second. After processing messages, related
 * methods will be invoked by this class.
 */
public class OServer {

    private static final int TCP_PORT = 1234;
    private static final int UDP_PORT = 1235;
    /**
     * Kyro server.
     */
    private Server server;
    private OMessageListener messageListener;

    /**
     * Queue object to store messages.
     */
    private Queue<Object> messageQueue;
    /**
     * Connection queue to store connections
     */
    private Queue<Connection> connectionQueue;
    /**
     * Queue to store disconnected connections to be processed on the game loop thread.
     */
    private Queue<Connection> disconnectedQueue;

    private Logger logger = Logger.getLogger(OServer.class);

    public OServer(OMessageListener cmo) {

        this.messageListener = cmo;

        init();
    }

    private void init() {

        server = new Server();
        registerClasses();

        messageQueue = new LinkedList<>();
        connectionQueue = new LinkedList<>();
        disconnectedQueue = new LinkedList<>();

        server.addListener(new Listener() {

            @Override
            public void received(Connection connection, Object object) {

                messageQueue.add(object);
                connectionQueue.add(connection);

            }

            @Override
            public void disconnected(Connection connection) {
                // Queue disconnections to be processed safely in parseMessage
                disconnectedQueue.add(connection);
            }
        });
        server.start();
        try {
            server.bind(TCP_PORT, UDP_PORT);
            logger.debug("Server has ben started on TCP_PORT: " + TCP_PORT + " UDP_PORT: " + UDP_PORT);
        } catch (IOException e) {
            logger.log(Level.ALL, e);
        }

    }

    /**
     * Gets messages from connection and message queues,parse them and invokes
     * necessary methods.
     * <p>
     * If one those queues is empty, it returns.
     * <p>
     * This method will be called 60 per second.
     */
    public void parseMessage() {

        if (connectionQueue.isEmpty() || messageQueue.isEmpty())
            return;

        for (int i = 0, n = messageQueue.size(); i < n; i++) {

            Connection con = connectionQueue.poll();
            Object message = messageQueue.poll();

            if (message instanceof LoginMessage m) {

                messageListener.loginReceived(con, m);

            } else if (message instanceof LogoutMessage m) {
                messageListener.logoutReceived(m);

            } else if (message instanceof PositionMessage m) {
                messageListener.playerMovedReceived(m);

            } else if (message instanceof ShootMessage m) {
                messageListener.shootReceived(m);
            }
            else if (message instanceof WeaponChangeMessage m) {
                messageListener.weaponChangeReceived(m);
            }
            else if (message instanceof PlaceSpikeMessage m) {
                messageListener.placeSpikeReceived(m);
            }
            else if (message instanceof UndoSpikeMessage m) {
                messageListener.undoSpikeReceived(m);
            }

        }

        // Process disconnect events after messages to ensure consistent state updates
        while (!disconnectedQueue.isEmpty()) {
            Connection dc = disconnectedQueue.poll();
            messageListener.disconnected(dc);
        }

    }

    /**
     * This function register every class that will be sent back and forth between
     * client and server.
     */
    private void registerClasses() {
        // messages
        this.server.getKryo().register(LoginMessage.class);
        this.server.getKryo().register(LogoutMessage.class);
        this.server.getKryo().register(GameWorldMessage.class);
        this.server.getKryo().register(PositionMessage.class);
        this.server.getKryo().register(PositionMessage.Direction.class);
        this.server.getKryo().register(ShootMessage.class);
        this.server.getKryo().register(PlayerDiedMessage.class);

        this.server.getKryo().register(WeaponChangeMessage.class);
        this.server.getKryo().register(WeaponInfoMessage.class);
        
        this.server.getKryo().register(PlaceSpikeMessage.class);
        this.server.getKryo().register(UndoSpikeMessage.class);
        this.server.getKryo().register(InventoryUpdateMessage.class);

        // primitive arrays
        this.server.getKryo().register(float[].class);
    }

    public void sendToAllUDP(Object m) {
        server.sendToAllUDP(m);
    }

    public void sendToUDP(int id, Object m) {
        server.sendToUDP(id, m);
    }
}
