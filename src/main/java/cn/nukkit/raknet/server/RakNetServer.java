package cn.nukkit.raknet.server;

import cn.nukkit.raknet.RakNet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.log4j.Log4j2;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@Log4j2
public class RakNetServer extends Thread {

    protected final int port;
    protected String interfaz;

    protected boolean shutdown;

    protected ConcurrentLinkedQueue<byte[]> externalQueue;
    protected ConcurrentLinkedQueue<byte[]> internalQueue;

    protected long serverId = 0;
    protected short maxMtuSize;
    protected int protocolVersion;

    public RakNetServer(int port) {
        this(port, "0.0.0.0");
    }

    public RakNetServer(int port, String interfaz) {
        this(port, interfaz, (short) 1492);
    }

    public RakNetServer(int port, String interfaz, short maxMtuSize) {
        this(port, interfaz, maxMtuSize, RakNet.PROTOCOL);//RakNet.DEFAULT_PROTOCOL_VERSION
    }

    public RakNetServer(int port, String interfaz, short maxMtuSize, int overrideProtocolVersion) {
        this.port = port;
        if (port < 1 || port > 65536) {
            throw new IllegalArgumentException("Invalid port range");
        }
        this.interfaz = interfaz;

        this.serverId = ThreadLocalRandom.current().nextLong();
        this.maxMtuSize = maxMtuSize;

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
                log.fatal("RakNet crashed!");
            }
        }
    }

    @Override
    public void run() {
        this.setName("RakNet Thread #" + Thread.currentThread().getId());
        Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
        UDPServerSocket socket = new UDPServerSocket(this.port, this.interfaz);
        try {
            new SessionManager(this, socket, this.maxMtuSize);
        } catch (Exception e) {
            log.throwing(e);
        }
    }
}
