package cn.nukkit.raknet.protocol;

import cn.nukkit.raknet.RakNet;

public class IncompatibleProtocolVersion extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_INCOMPATIBLE_PROTOCOL_VERSION;

    @Override
    public byte getID() {
        return ID;
    }

    public int protocolVersion = 0;
    public long serverId = 0;

    @Override
    public Packet clean() {
        this.magic = new byte[16];
        return super.clean();
    }

    @Override
    public void encode() {
        super.encode();
        this.putByte(this.protocolVersion);
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
}
