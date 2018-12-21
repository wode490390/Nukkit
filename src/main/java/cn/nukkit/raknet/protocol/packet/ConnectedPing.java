package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ConnectedPing extends Packet {

    public static final byte ID = MessageIdentifiers.ID_CONNECTED_PING;

    @Override
    public byte getID() {
        return ID;
    }

    public long sendPingTime;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.sendPingTime);
    }

    @Override
    public void decode() {
        super.decode();
        this.sendPingTime = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new ConnectedPing();
        }

    }
}
