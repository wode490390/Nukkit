package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3;

/**
 * @author Nukkit Project Team
 */
public class RespawnPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESPAWN_PACKET;

    public Vector3 position;

    @Override
    public void decode() {
        this.position = this.getVector3().asVector3();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3(this.position.asVector3f());
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
