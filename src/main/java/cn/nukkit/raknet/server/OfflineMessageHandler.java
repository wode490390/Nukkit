package cn.nukkit.raknet.server;

import cn.nukkit.raknet.protocol.IncompatibleProtocolVersion;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.OpenConnectionReply1;
import cn.nukkit.raknet.protocol.OpenConnectionReply2;
import cn.nukkit.raknet.protocol.OpenConnectionRequest1;
import cn.nukkit.raknet.protocol.OpenConnectionRequest2;
import cn.nukkit.raknet.protocol.UnconnectedPing;
import cn.nukkit.raknet.protocol.UnconnectedPong;

public class OfflineMessageHandler {

    private RakNetServer sessionManager;

    public OfflineMessageHandler(SessionManager manager) {
        this.sessionManager = manager;
    }

    public boolean handle(OfflineMessage packet, String address, int port) {
        switch (packet.ID) {
            case UnconnectedPing.ID:
                UnconnectedPong pk = new UnconnectedPong();
                pk.serverID = this.sessionManager.getID();
                pk.pingID = packet.pingID;
                pk.serverName = this.sessionManager.getName();
                this.sessionManager.sendPacket(pk, address, port);
                return true;
            case OpenConnectionRequest1.ID:
                serverProtocol = this.sessionManager.getProtocolVersion();
                if (packet.protocol != serverProtocol) {
                    IncompatibleProtocolVersion pk = new IncompatibleProtocolVersion();
                    pk.protocolVersion = serverProtocol;
                    pk.serverId = this.sessionManager.getID();
                    this.sessionManager.sendPacket(pk, address, port);
                    this.sessionManager.getLogger().notice("Refused connection from address due to incompatible RakNet protocol version (expected " + serverProtocol + ", got " + packet.protocol + ")");
                } else {
                    OpenConnectionReply1 pk = new OpenConnectionReply1();
                    pk.mtuSize = packet.mtuSize + 28; //IP header size (20 bytes) + UDP header size (8 bytes)
                    pk.serverID = this.sessionManager.getID();
                    this.sessionManager.sendPacket(pk, address, port);
                }
                return true;
            case OpenConnectionRequest2.ID:
                if (packet.serverPort == this.sessionManager.getPort() || !this.sessionManager.portChecking) {
                    short mtuSize = Math.min(Math.abs(packet.mtuSize), this.sessionManager.getMaxMtuSize()); //Max size, do not allow creating large buffers to fill server memory
                    OpenConnectionReply2 pk = new OpenConnectionReply2();
                    pk.mtuSize = mtuSize;
                    pk.serverID = this.sessionManager.getID();
                    pk.clientAddress = address;
                    pk.clientPort = port;
                    this.sessionManager.sendPacket(pk, address, port);
                    this.sessionManager.createSession(address, port, packet.clientID, mtuSize);
                } else {
                    this.sessionManager.getLogger().debug("Not creating session for address due to mismatched port, expected " + this.sessionManager.getPort() + ", got " + packet.serverPort);
                }
                return true;
        }

        return false;
    }
}
