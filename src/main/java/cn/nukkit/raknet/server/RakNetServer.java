package cn.nukkit.raknet.server;

import cn.nukkit.Server;
import cn.nukkit.raknet.RakNet;
import cn.nukkit.utils.ThreadedLogger;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class RakNetServer extends Thread {

    protected final int port;
    protected String interfaz;

    protected ThreadedLogger logger;

    protected boolean shutdown;

    protected ConcurrentLinkedQueue<byte[]> externalQueue;
    protected ConcurrentLinkedQueue<byte[]> internalQueue;

    protected long serverId = 0;
    protected short maxMtuSize;
    protected int protocolVersion;

    public RakNetServer(ThreadedLogger logger, int port) {
        this(logger, port, "0.0.0.0");
    }

    public RakNetServer(ThreadedLogger logger, int port, String interfaz) {
        this(logger, port, interfaz, (short) 1492);
    }

    public RakNetServer(ThreadedLogger logger, int port, String interfaz, short maxMtuSize) {
        this(logger, port, interfaz, maxMtuSize, RakNet.PROTOCOL);//RakNet.DEFAULT_PROTOCOL_VERSION
    }

    public RakNetServer(ThreadedLogger logger, int port, String interfaz, short maxMtuSize, int overrideProtocolVersion) {
        this.port = port;
        if (port < 1 || port > 65536) {
            throw new IllegalArgumentException("Invalid port range");
        }
        this.interfaz = interfaz;
        
        this.serverId = new Random().nextLong();
        this.maxMtuSize = maxMtuSize;

        this.logger = logger;

        this.externalQueue = new ConcurrentLinkedQueue<>();
        this.internalQueue = new ConcurrentLinkedQueue<>();

        this.protocolVersion = overrideProtocolVersion;

        this.start();
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public void shutdown() {
        this.shutdown = true;
    }

    public int getPort() {
        return this.port;
    }

    public String getInterface() {
        return interfaz;
    }

    public long getServerId() {
        return this.serverId;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public ThreadedLogger getLogger() {
        return logger;
    }

    public ConcurrentLinkedQueue<byte[]> getExternalQueue() {
        return this.externalQueue;
    }

    public ConcurrentLinkedQueue<byte[]> getInternalQueue() {
        return this.internalQueue;
    }

    public void pushMainToThreadPacket(byte[] data) {
        this.internalQueue.add(data);
    }

    public byte[] readMainToThreadPacket() {
        return this.internalQueue.poll();
    }

    public void pushThreadToMainPacket(byte[] data) {
        this.externalQueue.add(data);
    }

    public byte[] readThreadToMainPacket() {
        return this.externalQueue.poll();
    }

    private class ShutdownHandler extends Thread {
        @Override
        public void run() {
            if (!shutdown) {
                logger.emergency("RakNet crashed!");
            }
        }
    }

    @Override
    public void run() {
        this.setName("RakNet Thread #" + Thread.currentThread().getId());
        Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
        UDPServerSocket socket = new UDPServerSocket(this.getLogger(), this.port, this.interfaz);
        try {
            new SessionManager(this, socket, this.maxMtuSize);
        } catch (Exception e) {
            Server.getInstance().getLogger().logException(e);
        }
    }
}
