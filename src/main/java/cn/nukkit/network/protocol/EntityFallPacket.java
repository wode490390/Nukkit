package cn.nukkit.network.protocol;

public class EntityFallPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ENTITY_FALL_PACKET;

    public long entityRuntimeId;
    public float fallDistance;
    public boolean isInVoid;

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.fallDistance = this.getLFloat();
        this.isInVoid = this.getBoolean();
    }

    @Override
    public void encode() {

    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
