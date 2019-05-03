package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class EntityFallPacket extends DataPacket implements ServerboundPacket {

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
