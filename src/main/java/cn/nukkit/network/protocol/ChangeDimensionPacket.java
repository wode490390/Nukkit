package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3;

/**
 * Created on 2016/1/5 by xtypr.
 * Package cn.nukkit.network.protocol in project nukkit .
 */
public class ChangeDimensionPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CHANGE_DIMENSION_PACKET;

    public int dimension;
    public Vector3 position;
    public boolean respawn = false;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.dimension);
        this.putVector3(this.position.asVector3f());
        this.putBoolean(this.respawn);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
