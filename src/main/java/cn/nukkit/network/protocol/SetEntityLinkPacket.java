package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.EntityLink;

/**
 * Created on 15-10-22.
 */
public class SetEntityLinkPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_LINK_PACKET;

    public EntityLink link;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityLink(this.link);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
