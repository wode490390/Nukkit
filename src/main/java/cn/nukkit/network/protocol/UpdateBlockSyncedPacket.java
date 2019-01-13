package cn.nukkit.network.protocol;

public class UpdateBlockSyncedPacket extends UpdateBlockPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_BLOCK_SYNCED_PACKET;

    public long entityUniqueId = 0;
    public long uvarint64_2 = 0;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        super.decode();
        this.entityUniqueId = this.getUnsignedVarLong();
        this.uvarint64_2 = this.getUnsignedVarLong();
    }

    @Override
    public void encode() {
        super.encode();
        this.putUnsignedVarLong(this.entityUniqueId);
        this.putUnsignedVarLong(this.uvarint64_2);
    }
}
