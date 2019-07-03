package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.DimensionIds;
import lombok.ToString;

@ToString
public class SpawnParticleEffectPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SPAWN_PARTICLE_EFFECT_PACKET;

    public int dimensionId = DimensionIds.OVERWORLD; //wtf mojang
    public long entityUniqueId = -1; //default none
    public Vector3f position;
    public String particleName;

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
        this.putByte((byte) this.dimensionId);
        this.putEntityUniqueId(this.entityUniqueId);
        this.putVector3(this.position);
        this.putString(this.particleName);
    }
}
