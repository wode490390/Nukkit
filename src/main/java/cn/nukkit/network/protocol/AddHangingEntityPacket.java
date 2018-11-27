package cn.nukkit.network.protocol;

public class AddHangingEntityPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_HANGING_ENTITY_PACKET;

    public long entityUniqueId;
    public int entityRuntimeId;
    public int x;
    public int y;
    public int z;
    public int direction;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId || this.entityRuntimeId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putBlockPosition(this.x, this.y, this.z);
        this.putVarInt(this.direction);
    }
}
