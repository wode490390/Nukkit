package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class SetLastHurtByPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_LAST_HURT_BY_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int entityTypeId;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.entityTypeId);
    }
}
