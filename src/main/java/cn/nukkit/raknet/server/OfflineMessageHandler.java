package cn.nukkit.raknet.server;

import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.packet.IncompatibleProtocolVersion;
import cn.nukkit.raknet.protocol.packet.OpenConnectionReply1;
import cn.nukkit.raknet.protocol.packet.OpenConnectionReply2;
import cn.nukkit.raknet.protocol.packet.OpenConnectionRequest1;
import cn.nukkit.raknet.protocol.packet.OpenConnectionRequest2;
import cn.nukkit.raknet.protocol.packet.UnconnectedPing;
import cn.nukkit.raknet.protocol.packet.UnconnectedPong;

import java.io.IOException;

public class OfflineMessageHandler {

    private final SessionManager sessionManager;

    public OfflineMessageHandler(SessionManager manager) {
        this.sessionManager = manager;
    }

    public boolean handle(OfflineMessage packet, String address, int port) {
        switch (packet.ID) {
            case UnconnectedPing.ID:
                UnconnectedPong pk1 = new UnconnectedPong();
                pk1.serverID = this.sessionManager.getID();
                pk1.pingID = ((UnconnectedPong) packet).pingID;
                pk1.serverName = this.sessionManager.getName();
                try {
                    this.sessionManager.sendPacket(pk1, address, port);
                } catch (IOException e) {
                    //ignore
                }
                return true;
            case OpenConnectionRequest1.ID:
                int serverProtocol = this.sessionManager.getProtocolVersion();
                if (((OpenConnectionRequest1) packet).protocol != serverProtocol) {
                    IncompatibleProtocolVersion pk2 = new IncompatibleProtocolVersion();
                    pk2.protocolVersion = serverProtocol;
                    pk2.serverId = this.sessionManager.getID();
                    try {
                        this.sessionManager.sendPacket(pk2, address, port);
                    } catch (IOException e) {
                        //ignore
                    }
                    this.sessionManager.getLogger().notice("Refused connection from address due to incompatible RakNet protocol version (expected " + serverProtocol + ", got " + ((OpenConnectionRequest1) packet).protocol + ")");
                } else {
                    OpenConnectionReply1 pk3 = new OpenConnectionReply1();
                    pk3.mtuSize = (short) (((OpenConnectionRequest1) packet).mtuSize + 28); //IP header size (20 bytes) + UDP header size (8 bytes)
                    pk3.serverID = this.sessionManager.getID();
                    try {
                        this.sessionManager.sendPacket(pk3, address, port);
                    } catch (IOException e) {
                        //ignore
                    }
                }
                return true;
            case OpenConnectionRequest2.ID:
                if (((OpenConnectionRequest2) packet).serverPort == this.sessionManager.getPort() || !this.sessionManager.portChecking) {
                    short mtuSize = (short) Math.min(Math.abs(((OpenConnectionRequest2) packet).mtuSize), this.sessionManager.getMaxMtuSize()); //Max size, do not allow creating large buffers to fill server memory
                    OpenConnectionReply2 pk = new OpenConnectionReply2();
                    pk.mtuSize = mtuSize;
                    pk.serverID = this.sessionManager.getID();
                    pk.clientAddress = address;
                    pk.clientPort = port;
                    try {
                        this.sessionManager.sendPacket(pk, address, port);
                    } catch (IOException e) {
                        //ignore
                    }
                    this.sessionManager.createSession(address, port, ((OpenConnectionRequest2) packet).clientID, mtuSize);
                } else {
                    this.sessionManager.getLogger().debug("Not creating session for address due to mismatched port, expected " + this.sessionManager.getPort() + ", got " + ((OpenConnectionRequest2) packet).serverPort);
                }
                return true;
        }

        return false;
    }
}
