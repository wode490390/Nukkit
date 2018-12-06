package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class UnconnectedPing extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_UNCONNECTED_PING;

    @Override
    public byte getID() {
        return ID;
    }

    public long pingID;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
        this.writeMagic();
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
        this.readMagic();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new UnconnectedPing();
        }

    }
}
