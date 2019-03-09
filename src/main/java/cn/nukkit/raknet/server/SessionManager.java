package cn.nukkit.raknet.server;

import cn.nukkit.raknet.RakNet;
import cn.nukkit.raknet.protocol.EncapsulatedPacket;
import cn.nukkit.raknet.protocol.Packet;
import cn.nukkit.raknet.protocol.packet.ACK;
import cn.nukkit.raknet.protocol.packet.AdvertiseSystem;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_0;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_1;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_2;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_3;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_4;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_5;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_6;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_7;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_8;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_9;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_A;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_B;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_C;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_D;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_E;
import cn.nukkit.raknet.protocol.packet.DATA_PACKET_F;
import cn.nukkit.raknet.protocol.packet.NACK;
import cn.nukkit.raknet.protocol.packet.OpenConnectionReply1;
import cn.nukkit.raknet.protocol.packet.OpenConnectionReply2;
import cn.nukkit.raknet.protocol.packet.OpenConnectionRequest1;
import cn.nukkit.raknet.protocol.packet.OpenConnectionRequest2;
import cn.nukkit.raknet.protocol.packet.UnconnectedPing;
import cn.nukkit.raknet.protocol.packet.UnconnectedPingOpenConnections;
import cn.nukkit.raknet.protocol.packet.UnconnectedPong;
import cn.nukkit.utils.Binary;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@Log4j2
public class SessionManager {

    protected final Packet.PacketFactory[] packetPool = new Packet.PacketFactory[256];

    protected final RakNetServer server;
    protected final UDPServerSocket socket;

    protected int receiveBytes = 0;
    protected int sendBytes = 0;

    protected final Map<String, Session> sessions = new HashMap<>();

    protected OfflineMessageHandler offlineMessageHandler;
    protected String name = "";

    protected int packetLimit = 1000;

    protected boolean shutdown = false;

    protected long ticks = 0;
    protected long lastMeasure;

    protected final Map<String, Long> block = new HashMap<>();
    protected final Map<String, Integer> ipSec = new HashMap<>();
    protected Pattern rawPacketFilters;

    public boolean portChecking = true;

    protected long startTimeMS;

    protected short maxMtuSize;

    protected String reusableAddress;

    public final long serverId;

    protected String currentSource = "";

    public SessionManager(RakNetServer server, UDPServerSocket socket, short maxMtuSize) throws Exception {
        this.server = server;
        this.socket = socket;

        this.startTimeMS = System.currentTimeMillis();
        this.maxMtuSize = maxMtuSize;

        this.offlineMessageHandler = new OfflineMessageHandler(this);

        this.reusableAddress = this.socket.getBindAddress();

        this.serverId = ThreadLocalRandom.current().nextLong();

        this.registerPackets();

        this.run();
    }

    /**
     * Returns the time in milliseconds since server start.
     * @return int
     */
    public long getRakNetTimeMS() {
        return System.currentTimeMillis() - this.startTimeMS;
    }

    public int getPort() {
        return this.server.port;
    }

    public short getMaxMtuSize() {
        return this.maxMtuSize;
    }

    public int getProtocolVersion() {
        return this.server.getProtocolVersion();
    }

    public void run() throws Exception {
        this.tickProcessor();
    }

    private void tickProcessor() throws Exception {
        this.lastMeasure = System.currentTimeMillis();
        while (!this.shutdown) {
            long start = System.currentTimeMillis();
            int max = 1000;
            while (max > 0) {
                try {
                    if (!this.receivePacket()) {
                        break;
                    }
                    --max;
                } catch (Exception e) {
                    if (!currentSource.isEmpty()) {
                        this.blockAddress(currentSource);
                    }
                    // else ignore
                }
            }
            while (this.receiveStream()) ;

            long time = System.currentTimeMillis() - start;
            if (time < 10) {
                try {
                    Thread.sleep(10 - time);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
            this.tick();
        }
    }

    private void tick() throws Exception {
        long time = System.currentTimeMillis();
        for (Session session : new ArrayList<>(this.sessions.values())) {
            session.update(time);
        }

        for (String address : this.ipSec.keySet()) {
            int count = this.ipSec.get(address);
            if (count >= this.packetLimit) {
                this.blockAddress(address);
            }
        }
        this.ipSec.clear();

        if ((this.ticks & 0b1111) == 0) {
            double diff = Math.max(5d, (double) time - this.lastMeasure);
            this.streamOption("bandwidth", this.sendBytes / diff + ";" + this.receiveBytes / diff);
            this.lastMeasure = time;
            this.sendBytes = 0;
            this.receiveBytes = 0;

            if (!this.block.isEmpty()) {
                long now = System.currentTimeMillis();
                for (String address : new ArrayList<>(this.block.keySet())) {
                    long timeout = this.block.get(address);
                    if (timeout <= now) {
                        this.block.remove(address);
                        log.info("Unblocked " + address);
                    } else {
                        break;
                    }
                }
            }
        }

        ++this.ticks;
    }

    private boolean receivePacket() throws Exception {
        DatagramPacket datagramPacket = this.socket.readPacket();
        if (datagramPacket != null) {
            // Check this early
            try {
                String source = datagramPacket.sender().getHostString();
                currentSource = source; //in order to block address
                if (this.block.containsKey(source)) {
                    return true;
                }

                if (this.ipSec.containsKey(source)) {
                    this.ipSec.put(source, this.ipSec.get(source) + 1);
                } else {
                    this.ipSec.put(source, 1);
                }

                ByteBuf byteBuf = datagramPacket.content();
                if (byteBuf.readableBytes() == 0) {
                    // Exit early to process another packet
                    return true;
                }
                byte[] buffer = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(buffer);
                int len = buffer.length;
                int port = datagramPacket.sender().getPort();

                this.receiveBytes += len;

                byte pid = buffer[0];

                if (pid == UnconnectedPong.ID) {
                    return false;
                }

                Packet packet = this.getPacketFromPool(pid);
                if (packet != null) {
                    packet.buffer = buffer;
                    this.getSession(source, port).handlePacket(packet);
                    return true;
                } else if (pid == UnconnectedPing.ID) {
                    packet = new UnconnectedPing();
                    packet.buffer = buffer;
                    packet.decode();

                    UnconnectedPong pk = new UnconnectedPong();
                    pk.serverID = this.getID();
                    pk.pingID = ((UnconnectedPing) packet).pingID;
                    pk.serverName = this.getName();
                    this.sendPacket(pk, source, port);
                } else if (buffer.length != 0) {
                    this.streamRaw(source, port, buffer);
                    return true;
                } else {
                    return false;
                }
            } finally {
                datagramPacket.release();
            }
        }

        return false;
    }

    public void sendPacket(Packet packet, String dest, int port) throws IOException {
        packet.encode();
        this.sendBytes += this.socket.writePacket(packet.buffer, dest, port);
    }

    public void sendPacket(Packet packet, InetSocketAddress dest) throws IOException {
        packet.encode();
        this.sendBytes += this.socket.writePacket(packet.buffer, dest);
    }

    public void streamEncapsulated(Session session, EncapsulatedPacket packet) {
        this.streamEncapsulated(session, packet, RakNet.PRIORITY_NORMAL);
    }

    public void streamEncapsulated(Session session, EncapsulatedPacket packet, int flags) {
        String id = session.getAddress() + ":" + session.getPort();
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_ENCAPSULATED,
                new byte[]{(byte) (id.length() & 0xff)},
                id.getBytes(StandardCharsets.UTF_8),
                new byte[]{(byte) (flags & 0xff)},
                packet.toBinary(true)
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    public void streamRaw(String address, int port, byte[] payload) {
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_RAW,
                new byte[]{(byte) (address.length() & 0xff)},
                address.getBytes(StandardCharsets.UTF_8),
                Binary.writeShort(port),
                payload
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    protected void streamClose(String identifier, String reason) {
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_CLOSE_SESSION,
                new byte[]{(byte) (identifier.length() & 0xff)},
                identifier.getBytes(StandardCharsets.UTF_8),
                new byte[]{(byte) (reason.length() & 0xff)},
                reason.getBytes(StandardCharsets.UTF_8)
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    protected void streamInvalid(String identifier) {
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_INVALID_SESSION,
                new byte[]{(byte) (identifier.length() & 0xff)},
                identifier.getBytes(StandardCharsets.UTF_8)
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    protected void streamOpen(Session session) {
        String identifier = session.getAddress() + ":" + session.getPort();
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_OPEN_SESSION,
                new byte[]{(byte) (identifier.length() & 0xff)},
                identifier.getBytes(StandardCharsets.UTF_8),
                new byte[]{(byte) (session.getAddress().length() & 0xff)},
                session.getAddress().getBytes(StandardCharsets.UTF_8),
                Binary.writeShort(session.getPort()),
                Binary.writeLong(session.getID())
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    protected void streamACK(String identifier, int identifierACK) {
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_ACK_NOTIFICATION,
                new byte[]{(byte) (identifier.length() & 0xff)},
                identifier.getBytes(StandardCharsets.UTF_8),
                Binary.writeInt(identifierACK)
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    protected void streamOption(String name, String value) {
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_SET_OPTION,
                new byte[]{(byte) (name.length() & 0xff)},
                name.getBytes(StandardCharsets.UTF_8),
                value.getBytes(StandardCharsets.UTF_8)
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    protected void streamPingMeasure(Session session, long pingMS) {
        String identifier = session.getAddress() + ":" + session.getPort();
        byte[] buffer = Binary.appendBytes(
                ITCProtocol.PACKET_REPORT_PING,
                new byte[]{(byte) (identifier.length() & 0xff)},
                identifier.getBytes(StandardCharsets.UTF_8),
                Binary.writeInt((int) pingMS)
        );
        this.server.pushThreadToMainPacket(buffer);
    }

    public boolean receiveStream() throws Exception {
        byte[] packet = this.server.readMainToThreadPacket();
        if (packet != null && packet.length > 0) {
            byte id = packet[0];
            int offset = 1;
            switch (id) {
                case ITCProtocol.PACKET_ENCAPSULATED:
                    int len = packet[offset++];
                    String identifier = new String(Binary.subBytes(packet, offset, len), StandardCharsets.UTF_8);
                    offset += len;
                    if (this.sessions.containsKey(identifier)) {
                        byte flags = packet[offset++];
                        byte[] buffer = Binary.subBytes(packet, offset);
                        this.sessions.get(identifier).addEncapsulatedToQueue(EncapsulatedPacket.fromBinary(buffer, true), flags);
                    } else {
                        this.streamInvalid(identifier);
                    }
                    break;
                case ITCProtocol.PACKET_RAW:
                    len = packet[offset++];
                    String address = new String(Binary.subBytes(packet, offset, len), StandardCharsets.UTF_8);
                    offset += len;
                    int port = Binary.readShort(Binary.subBytes(packet, offset, 2));
                    offset += 2;
                    byte[] payload = Binary.subBytes(packet, offset);
                    this.socket.writePacket(payload, address, port);
                    break;
                case ITCProtocol.PACKET_CLOSE_SESSION:
                    len = packet[offset++];
                    identifier = new String(Binary.subBytes(packet, offset, len), StandardCharsets.UTF_8);
                    if (this.sessions.containsKey(identifier)) {
                        this.removeSession(this.sessions.get(identifier));
                    } else {
                        this.streamInvalid(identifier);
                    }
                    break;
                case ITCProtocol.PACKET_INVALID_SESSION:
                    len = packet[offset++];
                    identifier = new String(Binary.subBytes(packet, offset, len), StandardCharsets.UTF_8);
                    if (this.sessions.containsKey(identifier)) {
                        this.removeSession(this.sessions.get(identifier));
                    }
                    break;
                case ITCProtocol.PACKET_SET_OPTION:
                    len = packet[offset++];
                    String name = new String(Binary.subBytes(packet, offset, len), StandardCharsets.UTF_8);
                    offset += len;
                    String value = new String(Binary.subBytes(packet, offset), StandardCharsets.UTF_8);
                    switch (name) {
                        case "name":
                            this.name = value;
                            break;
                        case "portChecking":
                            this.portChecking = Boolean.valueOf(value);
                            break;
                        case "packetLimit":
                            this.packetLimit = Integer.valueOf(value);
                            break;
                    }
                    break;
                case ITCProtocol.PACKET_BLOCK_ADDRESS:
                    len = packet[offset++];
                    address = new String(Binary.subBytes(packet, offset, len), StandardCharsets.UTF_8);
                    offset += len;
                    int timeout = Binary.readInt(Binary.subBytes(packet, offset, 4));
                    this.blockAddress(address, timeout);
                    break;
                case ITCProtocol.PACKET_UNBLOCK_ADDRESS:
                    len = packet[offset++];
                    address = new String(Binary.subBytes(packet, offset, len), StandardCharsets.UTF_8);
                    this.unblockAddress(address);
                    break;
                case ITCProtocol.PACKET_RAW_FILTER:
                    String pattern = new String(Binary.subBytes(packet, offset), StandardCharsets.UTF_8);
                    this.rawPacketFilters = Pattern.compile(pattern);
                    break;
                case ITCProtocol.PACKET_SHUTDOWN:
                    for (Session session : new ArrayList<>(this.sessions.values())) {
                        this.removeSession(session);
                    }

                    this.socket.close();
                    this.shutdown = true;
                    break;
                case ITCProtocol.PACKET_EMERGENCY_SHUTDOWN:
                    this.shutdown = true;
                    break;
                default:
                    log.debug("Unknown RakNet internal packet (ID " + id + ") received from main thread");
                    return false;
            }
            return true;
        }

        return false;
    }

    public void blockAddress(String address) {
        this.blockAddress(address, 300);
    }

    public void blockAddress(String address, int timeout) {
        long finalTime = System.currentTimeMillis() + timeout * 1000;
        if (!this.block.containsKey(address) || timeout == -1) {
            if (timeout == -1) {
                finalTime = Long.MAX_VALUE;
            } else {
                log.info("Blocked " + address + " for " + timeout + " seconds");
            }
            this.block.put(address, finalTime);
        } else if (this.block.get(address) < finalTime) {
            this.block.put(address, finalTime);
        }
    }

    public void unblockAddress(String address) {
        this.block.remove(address);
        log.debug("Unblocked " + address);
    }

    public Session getSession(String ip, int port) {
        String id = ip + ":" + port;
        if (!this.sessions.containsKey(id)) {
            this.checkSessions();
            Session session = new Session(this, ip, port, 0, this.getMaxMtuSize());
            this.sessions.put(id, session);

            return session;
        }

        return this.sessions.get(id);
    }

    public boolean sessionExists(String ip, int port) {
        String id = ip + ":" + port;
        return this.sessions.containsKey(id);
    }

    public Session createSession(String ip, int port, long clientId, short mtuSize) {
        String id = ip + ":" + port;
        this.checkSessions();
        Session session = new Session(this, ip, port, clientId, mtuSize);
        this.sessions.put(id, session);
        log.debug("Created session for " + id + " with MTU size " + mtuSize);
        return session;
    }

    public void removeSession(Session session) throws Exception {
        this.removeSession(session, "unknown");
    }

    public void removeSession(Session session, String reason) throws Exception {
        String id = session.getAddress() + ":" + session.getPort();
        if (this.sessions.containsKey(id)) {
            this.sessions.get(id).close();
            this.removeSessionInternal(session);
            this.streamClose(id, reason);
        }
    }

    public void removeSessionInternal(Session session) throws Exception {
        String id = session.getAddress() + ":" + session.getPort();
        this.sessions.remove(id);
    }

    public void openSession(Session session) {
        this.streamOpen(session);
    }

    private void checkSessions() {
        int size = this.sessions.size();
        if (size > 4096) {
            List<String> keyToRemove = new ArrayList<>();
            for (String i : this.sessions.keySet()) {
                Session s = this.sessions.get(i);
                if (s.isTemporal()) {
                    keyToRemove.add(i);
                    size--;
                    if (size <= 4096) {
                        break;
                    }
                }
            }

            for (String i : keyToRemove) {
                this.sessions.remove(i);
            }
        }
    }

    public void notifyACK(Session session, int identifierACK) {
        this.streamACK(session.getAddress() + ":" + session.getPort(), identifierACK);
    }

    public String getName() {
        return this.name;
    }

    public long getID() {
        return this.serverId;
    }

    private void registerPacket(byte id, Packet.PacketFactory factory) {
        this.packetPool[id & 0xFF] = factory;
    }

    public Packet getPacketFromPool(byte id) {
        return this.packetPool[id & 0xFF].create();
    }

    private void registerPackets() {
        // fill with dummy returning null
        Arrays.fill(this.packetPool, (Packet.PacketFactory) () -> null);

        //this.registerPacket(UnconnectedPing.ID, new UnconnectedPing.Factory());
        this.registerPacket(UnconnectedPingOpenConnections.ID, new UnconnectedPingOpenConnections.Factory());
        this.registerPacket(OpenConnectionRequest1.ID, new OpenConnectionRequest1.Factory());
        this.registerPacket(OpenConnectionReply1.ID, new OpenConnectionReply1.Factory());
        this.registerPacket(OpenConnectionRequest2.ID, new OpenConnectionRequest2.Factory());
        this.registerPacket(OpenConnectionReply2.ID, new OpenConnectionReply2.Factory());
        this.registerPacket(UnconnectedPong.ID, new UnconnectedPong.Factory());
        this.registerPacket(AdvertiseSystem.ID, new AdvertiseSystem.Factory());
        this.registerPacket(DATA_PACKET_0.ID, new DATA_PACKET_0.Factory());
        this.registerPacket(DATA_PACKET_1.ID, new DATA_PACKET_1.Factory());
        this.registerPacket(DATA_PACKET_2.ID, new DATA_PACKET_2.Factory());
        this.registerPacket(DATA_PACKET_3.ID, new DATA_PACKET_3.Factory());
        this.registerPacket(DATA_PACKET_4.ID, new DATA_PACKET_4.Factory());
        this.registerPacket(DATA_PACKET_5.ID, new DATA_PACKET_5.Factory());
        this.registerPacket(DATA_PACKET_6.ID, new DATA_PACKET_6.Factory());
        this.registerPacket(DATA_PACKET_7.ID, new DATA_PACKET_7.Factory());
        this.registerPacket(DATA_PACKET_8.ID, new DATA_PACKET_8.Factory());
        this.registerPacket(DATA_PACKET_9.ID, new DATA_PACKET_9.Factory());
        this.registerPacket(DATA_PACKET_A.ID, new DATA_PACKET_A.Factory());
        this.registerPacket(DATA_PACKET_B.ID, new DATA_PACKET_B.Factory());
        this.registerPacket(DATA_PACKET_C.ID, new DATA_PACKET_C.Factory());
        this.registerPacket(DATA_PACKET_D.ID, new DATA_PACKET_D.Factory());
        this.registerPacket(DATA_PACKET_E.ID, new DATA_PACKET_E.Factory());
        this.registerPacket(DATA_PACKET_F.ID, new DATA_PACKET_F.Factory());
        this.registerPacket(NACK.ID, new NACK.Factory());
        this.registerPacket(ACK.ID, new ACK.Factory());
    }
}
