package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class UnconnectedPingOpenConnections extends UnconnectedPing {

    public static final byte ID = MessageIdentifiers.ID_UNCONNECTED_PING_OPEN_CONNECTIONS;

    @Override
    public byte getID() {
        return ID;
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new UnconnectedPingOpenConnections();
        }

    }
}
