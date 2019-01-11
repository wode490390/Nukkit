package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;

/**
 * @author Nukkit Project Team
 */
public class RespawnPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESPAWN_PACKET;

    public Vector3f position;

    @Override
    public void decode() {
        this.position = this.getVector3();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3(this.position);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
