package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.Datagram;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_5 extends Datagram {

    public static final byte ID = (byte) 0x85;

    @Override
    public byte getID() {
        return ID;
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new DATA_PACKET_5();
        }

    }

}
