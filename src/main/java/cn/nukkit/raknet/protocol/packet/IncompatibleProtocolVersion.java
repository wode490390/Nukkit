package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.Packet;

public class IncompatibleProtocolVersion extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_INCOMPATIBLE_PROTOCOL_VERSION;

    @Override
    public byte getID() {
        return ID;
    }

    public int protocolVersion = 0;
    public long serverId = 0;

    @Override
    public void encode() {
        super.encode();
        this.putByte((byte) this.protocolVersion);
        this.writeMagic();
        this.putLong(this.serverId);
    }

    @Override
    public void decode() {
        super.decode();
        this.protocolVersion = this.getByte();
        this.readMagic();
        this.serverId = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new IncompatibleProtocolVersion();
        }

    }
}
