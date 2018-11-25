package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3;

public class SpawnExperienceOrbPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SPAWN_EXPERIENCE_ORB_PACKET;

    public Vector3 position;
    public int amount;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3(this.position.asVector3f());
        this.putVarInt(this.amount);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
