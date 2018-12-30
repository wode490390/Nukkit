package cn.nukkit.raknet.server;

import cn.nukkit.math.NukkitMath;
import cn.nukkit.raknet.RakNet;
import cn.nukkit.raknet.protocol.Datagram;
import cn.nukkit.raknet.protocol.EncapsulatedPacket;
import cn.nukkit.raknet.protocol.Packet;
import cn.nukkit.raknet.protocol.PacketReliability;
import cn.nukkit.raknet.protocol.packet.*;
import cn.nukkit.utils.Binary;
import cn.nukkit.utils.BinaryStream;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class Session {

    public final static int STATE_CONNECTING = 0;
    public final static int STATE_CONNECTED = 1;
    public final static int STATE_DISCONNECTING = 2;
    public final static int STATE_DISCONNECTED = 3;

    public final static int MAX_SPLIT_SIZE = 128;
    public final static int MAX_SPLIT_COUNT = 4;

    private final static int CHANNEL_COUNT = 32;

    public static final int WINDOW_SIZE = 2048;

    private static final short MAX_MTU_SIZE = 1492;
    private static final short MIN_MTU_SIZE = 400;

    private int messageIndex = 0;
    private final Map<Integer, Integer> channelIndex = new ConcurrentHashMap<Integer, Integer>();

    private SessionManager sessionManager;
    private final String address;
    private final int port;
    private int state = STATE_CONNECTING;
    //private List<EncapsulatedPacket> preJoinQueue = new ArrayList<>();
    private short mtuSize = MIN_MTU_SIZE;
    private long id = 0;
    private int splitID = 0;

    private int sendSeqNumber = 0;
    private int lastSeqNumber = -1;

    private long lastUpdate;
    private final long startTime;
    private long disconnectionTime;

    private boolean isTemporal = true;

    private final List<Datagram> packetToSend = new ArrayList<Datagram>();

    private boolean isActive = false;

    private Map<Integer, Integer> ACKQueue = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> NACKQueue = new HashMap<Integer, Integer>();

    private final Map<Integer, Datagram> recoveryQueue = new TreeMap<Integer, Datagram>();

    private final Map<Integer, Map<Integer, EncapsulatedPacket>> splitPackets = new HashMap<Integer, Map<Integer, EncapsulatedPacket>>();

    private final Map<Integer, Map<Integer, Integer>> needACK = new TreeMap<Integer, Map<Integer, Integer>>();

    private Datagram sendQueue;

    private int windowStart;
    private final Map<Integer, Integer> receivedWindow = new TreeMap<Integer, Integer>();
    private int windowEnd;
    private int highestSeqNumberThisTick = -1;

    private int reliableWindowStart;
    private int reliableWindowEnd;
    private final Map<Integer, EncapsulatedPacket> reliableWindow = new TreeMap<Integer, EncapsulatedPacket>();
    private int lastReliableIndex = -1;

    private long lastPingTime = -1;
    private long lastPingMeasure = 1;

    public Session(SessionManager sessionManager, String address, int port, long clientId, short mtuSize) {
        this.sessionManager = sessionManager;
        this.address = address;
        this.port = port;
        this.id = clientId;
        this.sendQueue = new DATA_PACKET_4();

        this.lastUpdate = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis();
        this.isActive = false;
        this.windowStart = -1;
        this.windowEnd = WINDOW_SIZE;

        this.reliableWindowStart = 0;
        this.reliableWindowEnd = WINDOW_SIZE;

        for (int i = 0; i < 32; i++) {
            this.channelIndex.put(i, 0);
        }

        this.mtuSize = mtuSize;
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public long getID() {
        return this.id;
    }

    public void update(long time) throws Exception {
        if (!this.isActive && (this.lastUpdate + 10000) < time) { //10 second timeout
            this.disconnect("timeout");

            return;
        }
        this.isActive = false;

        if (!this.ACKQueue.isEmpty()) {
            ACK pk = new ACK();
            pk.packets = new TreeMap<>(this.ACKQueue);
            this.sendPacket(pk);
            this.ACKQueue = new HashMap<>();
        }

        if (!this.NACKQueue.isEmpty()) {
            NACK pk = new NACK();
            pk.packets = new TreeMap<Integer, Integer>(this.NACKQueue);
            this.sendPacket(pk);
            this.NACKQueue = new HashMap<Integer, Integer>();
        }

        if (!this.packetToSend.isEmpty()) {
            int limit = 16;
            for (int i = 0; i < this.packetToSend.size(); i++) {
                Datagram pk = this.packetToSend.get(i);
                pk.sendTime = time;
                pk.encode();
                this.recoveryQueue.put(pk.seqNumber, pk);
                this.packetToSend.remove(pk);
                this.sendPacket(pk);

                if (limit-- <= 0) {
                    break;
                }
            }
        }

        if (this.packetToSend.size() > WINDOW_SIZE) {
            this.packetToSend.clear();
        }

        if (!this.needACK.isEmpty()) {
            for (int identifierACK : new ArrayList<>(this.needACK.keySet())) {
                Map<Integer, Integer> indexes = this.needACK.get(identifierACK);
                if (indexes.isEmpty()) {
                    this.needACK.remove(identifierACK);
                    this.sessionManager.notifyACK(this, identifierACK);
                }
            }
        }

        for (int seq : new ArrayList<>(this.recoveryQueue.keySet())) {
            Datagram pk = this.recoveryQueue.get(seq);
            if (pk.sendTime < System.currentTimeMillis() - 8000) {
                this.packetToSend.add(pk);
                this.recoveryQueue.remove(seq);
            } else {
                break;
            }
        }

        for (int seq : new ArrayList<>(this.receivedWindow.keySet())) {
            if (seq < this.windowStart) {
                this.receivedWindow.remove(seq);
            } else {
                break;
            }
        }

        this.sendQueue();
    }

    public void disconnect() throws Exception {
        this.disconnect("unknown");
    }

    public void disconnect(String reason) throws Exception {
        this.sessionManager.removeSession(this, reason);
    }

    private void sendDatagram(Datagram packet) throws IOException {
        this.sessionManager.sendPacket(packet, this.address, this.port);
    }

    private void queueConnectedPacket(Packet packet, int reliability, int orderChannel) throws IOException {
        this.queueConnectedPacket(packet, reliability, orderChannel, RakNet.PRIORITY_NORMAL);
    }

    private void queueConnectedPacket(Packet packet, int reliability, int orderChannel, int flags) throws IOException {
        packet.encode();

        EncapsulatedPacket encapsulated = new EncapsulatedPacket();
        encapsulated.reliability = reliability;
        encapsulated.orderChannel = orderChannel;
        encapsulated.buffer = packet.buffer;

        try {
            this.addEncapsulatedToQueue(encapsulated, flags);
        } catch (Exception e) {
            //ignore
        }
    }

    private void sendPacket(Packet packet) throws IOException {
        this.sessionManager.sendPacket(packet, this.address, this.port);
    }

    public void sendQueue() throws IOException {
        if (!this.sendQueue.packets.isEmpty()) {
            this.sendQueue.seqNumber = sendSeqNumber++;
            this.sendPacket(sendQueue);
            this.sendQueue.sendTime = System.currentTimeMillis();
            this.recoveryQueue.put(this.sendQueue.seqNumber, this.sendQueue);
            this.sendQueue = new DATA_PACKET_4();
        }
    }

    public void sendPing() throws Exception {
        this.sendPing(PacketReliability.UNRELIABLE);
    }

    public void sendPing(int reliability) throws Exception {
        ConnectedPing pk = new ConnectedPing();
        pk.sendPingTime = this.sessionManager.getRakNetTimeMS();
        this.queueConnectedPacket(pk, reliability, 0, RakNet.PRIORITY_IMMEDIATE);
    }

    private void addToQueue(EncapsulatedPacket pk) throws Exception {
        addToQueue(pk, RakNet.PRIORITY_NORMAL);
    }

    private void addToQueue(EncapsulatedPacket pk, int flags) throws Exception {
        int priority = flags & 0b0000111;
        if (pk.needACK && pk.messageIndex != null) {
            if (!this.needACK.containsKey(pk.identifierACK)) {
                this.needACK.put(pk.identifierACK, new HashMap<>());
            }
            this.needACK.get(pk.identifierACK).put(pk.messageIndex, pk.messageIndex);
        }

        if (priority == RakNet.PRIORITY_IMMEDIATE) { //Skip queues
            Datagram packet = new DATA_PACKET_0();
            packet.seqNumber = this.sendSeqNumber++;
            if (pk.needACK) {
                packet.packets.add(pk.clone());
                pk.needACK = false;
            } else {
                packet.packets.add(pk.toBinary());
            }

            this.sendPacket(packet);
            packet.sendTime = System.currentTimeMillis();
            this.recoveryQueue.put(packet.seqNumber, packet);

            return;
        }
        int length = this.sendQueue.length();
        if (length + pk.getTotalLength() > this.mtuSize) {
            this.sendQueue();
        }

        if (pk.needACK) {
            this.sendQueue.packets.add(pk.clone());
            pk.needACK = false;
        } else {
            this.sendQueue.packets.add(pk.toBinary());
        }
    }

    public void addEncapsulatedToQueue(EncapsulatedPacket packet) throws Exception {
        addEncapsulatedToQueue(packet, RakNet.PRIORITY_NORMAL);
    }

    public void addEncapsulatedToQueue(EncapsulatedPacket packet, int flags) throws Exception {
        if ((packet.needACK = (flags & RakNet.FLAG_NEED_ACK) > 0)) {
            this.needACK.put(packet.identifierACK, new HashMap<>());
        }

        if (packet.reliability == 2 ||
                packet.reliability == 3 ||
                packet.reliability == 4 ||
                packet.reliability == 6 ||
                packet.reliability == 7) {
            packet.messageIndex = this.messageIndex++;

            if (packet.reliability == 3) {
                int index = this.channelIndex.get(packet.orderChannel) + 1;
                packet.orderIndex = index;
                channelIndex.put(packet.orderChannel, index);
            }
        }

        if (packet.getTotalLength() + 4 > this.mtuSize) {
            byte[][] buffers = Binary.splitBytes(packet.buffer, this.mtuSize - 34);
            int splitID = ++this.splitID % 65536;
            for (int count = 0; count < buffers.length; count++) {
                byte[] buffer = buffers[count];
                EncapsulatedPacket pk = new EncapsulatedPacket();
                pk.splitID = splitID;
                pk.hasSplit = true;
                pk.splitCount = buffers.length;
                pk.reliability = packet.reliability;
                pk.splitIndex = count;
                pk.buffer = buffer;
                if (count > 0) {
                    pk.messageIndex = this.messageIndex++;
                } else {
                    pk.messageIndex = packet.messageIndex;
                }
                if (pk.reliability == 3) {
                    pk.orderChannel = packet.orderChannel;
                    pk.orderIndex = packet.orderIndex;
                }
                this.addToQueue(pk, flags | RakNet.PRIORITY_IMMEDIATE);
            }
        } else {
            this.addToQueue(packet, flags);
        }
    }

    private void handleSplit(EncapsulatedPacket packet) throws Exception {
        if (packet.splitCount >= MAX_SPLIT_SIZE || packet.splitIndex >= MAX_SPLIT_SIZE || packet.splitIndex < 0) {
            return;
        }

        if (!this.splitPackets.containsKey(packet.splitID)) {
            if (this.splitPackets.size() >= MAX_SPLIT_COUNT) {
                return;
            }
            this.splitPackets.put(packet.splitID, new HashMap<Integer, EncapsulatedPacket>() {{
                put(packet.splitIndex, packet);
            }});
        } else {
            this.splitPackets.get(packet.splitID).put(packet.splitIndex, packet);
        }

        if (this.splitPackets.get(packet.splitID).size() == packet.splitCount) {
            EncapsulatedPacket pk = new EncapsulatedPacket();
            BinaryStream stream = new BinaryStream();
            for (int i = 0; i < packet.splitCount; i++) {
                stream.put(this.splitPackets.get(packet.splitID).get(i).buffer);
            }
            pk.buffer = stream.getBuffer();
            pk.length = pk.buffer.length;
            this.splitPackets.remove(packet.splitID);

            this.handleEncapsulatedPacketRoute(pk);
        }
    }

    private void handleEncapsulatedPacket(EncapsulatedPacket packet) throws Exception {
        if (packet.messageIndex == null) {
            this.handleEncapsulatedPacketRoute(packet);
        } else {
            if (packet.messageIndex < this.reliableWindowStart || packet.messageIndex > this.reliableWindowEnd) {
                return;
            }

            if ((packet.messageIndex - this.lastReliableIndex) == 1) {
                this.lastReliableIndex++;
                this.reliableWindowStart++;
                this.reliableWindowEnd++;
                this.handleEncapsulatedPacketRoute(packet);

                if (!this.reliableWindow.isEmpty()) {
                    TreeMap<Integer, EncapsulatedPacket> sortedMap = new TreeMap<>(this.reliableWindow);

                    for (int index : sortedMap.keySet()) {
                        EncapsulatedPacket pk = this.reliableWindow.get(index);

                        if ((index - this.lastReliableIndex) != 1) {
                            break;
                        }

                        this.lastReliableIndex++;
                        this.reliableWindowStart++;
                        this.reliableWindowEnd++;
                        this.handleEncapsulatedPacketRoute(pk);
                        this.reliableWindow.remove(index);
                    }
                }
            } else {
                this.reliableWindow.put(packet.messageIndex, packet);
            }
        }

    }

    public int getState() {
        return state;
    }

    public boolean isTemporal() {
        return isTemporal;
    }

    public boolean isConnected() {
        return this.state != STATE_DISCONNECTING && this.state != STATE_DISCONNECTED;
    }

    private void handleEncapsulatedPacketRoute(EncapsulatedPacket packet) throws Exception {
        if (this.sessionManager == null) {
            return;
        }

        if (packet.hasSplit) {
            if (this.state == STATE_DISCONNECTED) {
                this.handleSplit(packet);
            }
            return;
        }

        byte id = packet.buffer[0];
        if ((id & 0xff) < 0x80) { //internal data packet
            if (state == STATE_DISCONNECTING) {
                if (id == ConnectionRequest.ID) {
                    ConnectionRequest dataPacket = new ConnectionRequest();
                    dataPacket.buffer = packet.buffer;
                    dataPacket.decode();
                    ConnectionRequestAccepted pk = new ConnectionRequestAccepted();
                    pk.address = this.address;
                    pk.port = this.port;
                    pk.sendPingTime = dataPacket.sendPingTime;
                    pk.sendPongTime = dataPacket.sendPingTime + 1000L;
                    pk.encode();

                    EncapsulatedPacket sendPacket = new EncapsulatedPacket();
                    sendPacket.reliability = 0;
                    sendPacket.buffer = pk.buffer;
                    this.addToQueue(sendPacket, RakNet.PRIORITY_IMMEDIATE);
                } else if (id == NewIncomingConnection.ID) {
                    NewIncomingConnection dataPacket = new NewIncomingConnection();
                    dataPacket.buffer = packet.buffer;
                    dataPacket.decode();

                    if (dataPacket.port == this.sessionManager.getPort() || !this.sessionManager.portChecking) {
                        this.state = STATE_DISCONNECTED; //FINALLY!
                        this.isTemporal = false;
                        this.sessionManager.openSession(this);
                    }
                }
            } else if (id == DisconnectionNotification.ID) {
                disconnect("client disconnect");
            } else if (id == ConnectedPing.ID) {
                ConnectedPing dataPacket = new ConnectedPing();
                dataPacket.buffer = packet.buffer;
                dataPacket.decode();

                ConnectedPong pk = new ConnectedPong();
                pk.sendPingTime = dataPacket.sendPingTime;
                pk.sendPongTime = this.sessionManager.getRakNetTimeMS();
                pk.encode();

                EncapsulatedPacket sendPacket = new EncapsulatedPacket();
                sendPacket.reliability = 0;
                sendPacket.buffer = pk.buffer;
                this.addToQueue(sendPacket);

                //Latency measurement
                ConnectedPing pingPacket = new ConnectedPing();
                pingPacket.sendPingTime = System.currentTimeMillis();
                pingPacket.encode();

                sendPacket = new EncapsulatedPacket();
                sendPacket.reliability = 0;
                sendPacket.buffer = pingPacket.buffer;
                this.addToQueue(sendPacket);
            } else if (id == ConnectedPong.ID) {
                if (state == STATE_DISCONNECTED) {
                    ConnectedPong dataPacket = new ConnectedPong();
                    dataPacket.buffer = packet.buffer;
                    dataPacket.decode();

                    if (state == STATE_DISCONNECTED) {
                        ConnectedPing pingPacket = new ConnectedPing();
                        pingPacket.sendPingTime = (System.currentTimeMillis() - dataPacket.sendPingTime) / 10;
                        pingPacket.encode();
                        packet.buffer = pingPacket.buffer;
                        this.sessionManager.streamEncapsulated(this, packet);
                    }
                }
            }
        } else if (state == STATE_DISCONNECTED) {
            this.sessionManager.streamEncapsulated(this, packet);
        } else {
            //this.sessionManager.getLogger().notice("Received packet before connection: "+Binary.bytesToHexString(packet.buffer));
        }
    }

    public void handlePong(long sendPingTime, long sendPongTime) throws Exception {
        this.lastPingMeasure = this.sessionManager.getRakNetTimeMS() - sendPingTime;
        this.sessionManager.streamPingMeasure(this, this.lastPingMeasure);
    }

    public void handlePacket(Packet packet) throws Exception {
        this.isActive = true;
        this.lastUpdate = System.currentTimeMillis();

        if (this.state == STATE_DISCONNECTED || this.state == STATE_DISCONNECTING) {
            if (((packet.buffer[0] & 0xff) >= 0x80 || (packet.buffer[0] & 0xff) <= 0x8f) && packet instanceof Datagram) {
                Datagram dp = (Datagram) packet;
                dp.decode();

                if (dp.seqNumber < this.windowStart || dp.seqNumber > this.windowEnd || this.receivedWindow.containsKey(dp.seqNumber)) {
                    return;
                }

                int diff = dp.seqNumber - this.lastSeqNumber;

                this.NACKQueue.remove(dp.seqNumber);
                this.ACKQueue.put(dp.seqNumber, dp.seqNumber);
                this.receivedWindow.put(dp.seqNumber, dp.seqNumber);

                if (diff != 1) {
                    for (int i = this.lastSeqNumber + 1; i < dp.seqNumber; i++) {
                        if (!this.receivedWindow.containsKey(i)) {
                            this.NACKQueue.put(i, i);
                        }
                    }
                }

                if (diff >= 1) {
                    this.lastSeqNumber = dp.seqNumber;
                    this.windowStart += diff;
                    this.windowEnd += diff;
                }

                for (Object pk : dp.packets) {
                    if (pk instanceof EncapsulatedPacket) {
                        this.handleEncapsulatedPacket((EncapsulatedPacket) pk);
                    }
                }
            } else {
                if (packet instanceof ACK) {
                    packet.decode();
                    for (int seq : new ArrayList<>(((ACK) packet).packets.values())) {
                        if (this.recoveryQueue.containsKey(seq)) {
                            for (Object pk : this.recoveryQueue.get(seq).packets) {
                                if (pk instanceof EncapsulatedPacket && ((EncapsulatedPacket) pk).needACK && ((EncapsulatedPacket) pk).messageIndex != null) {
                                    if (this.needACK.containsKey(((EncapsulatedPacket) pk).identifierACK)) {
                                        this.needACK.get(((EncapsulatedPacket) pk).identifierACK).remove(((EncapsulatedPacket) pk).messageIndex);
                                    }
                                }
                            }
                            this.recoveryQueue.remove(seq);
                        }
                    }
                } else if (packet instanceof NACK) {
                    packet.decode();
                    for (int seq : new ArrayList<>(((NACK) packet).packets.values())) {
                        if (this.recoveryQueue.containsKey(seq)) {
                            Datagram pk = this.recoveryQueue.get(seq);
                            pk.seqNumber = this.sendSeqNumber++;
                            this.packetToSend.add(pk);
                            this.recoveryQueue.remove(seq);
                        }
                    }
                }
            }
        } else if ((packet.buffer[0] & 0xff) > 0x00 || (packet.buffer[0] & 0xff) < 0x80) { //Not Data packet :)
            packet.decode();
            if (packet instanceof OpenConnectionRequest1) {
                //TODO: check protocol number and refuse connections
                OpenConnectionReply1 pk = new OpenConnectionReply1();
                pk.mtuSize = ((OpenConnectionRequest1) packet).mtuSize;
                pk.serverID = sessionManager.getID();
                this.sendPacket(pk);
                this.state = STATE_CONNECTED;
            } else if (this.state == STATE_CONNECTED && packet instanceof OpenConnectionRequest2) {
                this.id = ((OpenConnectionRequest2) packet).clientID;
                if (((OpenConnectionRequest2) packet).serverPort == this.sessionManager.getPort() || !this.sessionManager.portChecking) {
                    this.mtuSize = (short) NukkitMath.clamp(Math.abs(((OpenConnectionRequest2) packet).mtuSize), MIN_MTU_SIZE, MAX_MTU_SIZE);
                    OpenConnectionReply2 pk = new OpenConnectionReply2();
                    pk.mtuSize = this.mtuSize;
                    pk.serverID = this.sessionManager.getID();
                    pk.clientAddress = this.address;
                    pk.clientPort = this.port;
                    this.sendPacket(pk);
                    this.state = STATE_DISCONNECTING;
                }
            }
        }
    }

    public void flagForDisconnection() {
        this.state = STATE_DISCONNECTING;
        this.disconnectionTime = System.currentTimeMillis();
    }

    public void close() throws Exception {
        if(this.state != STATE_DISCONNECTED){
            this.state = STATE_DISCONNECTED;

            //TODO: the client will send an ACK for this, but we aren't handling it (debug spam)
            this.queueConnectedPacket(new DisconnectionNotification(), PacketReliability.RELIABLE_ORDERED, 0, RakNet.PRIORITY_IMMEDIATE);

            this.sessionManager.getLogger().debug("Closed session for " + this.address + ":" + this.port);
            this.sessionManager.removeSessionInternal(this);
            this.sessionManager = null;
        }
        /*byte[] data = new byte[]{0x60, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x15}; //CLIENT_DISCONNECT packet 0x15
        this.addEncapsulatedToQueue(EncapsulatedPacket.fromBinary(data));
        this.sessionManager = null;*/
    }
}
