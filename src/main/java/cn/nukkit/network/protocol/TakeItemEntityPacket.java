package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * Created on 15-10-14.
 */
@ToString
public class TakeItemEntityPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.TAKE_ITEM_ENTITY_PACKET;

    public long target;
    public long eid;

    @Override
    public void decode() {
        this.target = this.getEntityRuntimeId();
        this.eid = this.getEntityRuntimeId();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.target);
        this.putEntityRuntimeId(this.eid);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
