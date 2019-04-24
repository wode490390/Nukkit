package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;
import lombok.ToString;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@ToString
public class ExplodePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.EXPLODE_PACKET;

    public Vector3f position;
    public float radius;
    public Vector3f[] records = new Vector3f[0];

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public DataPacket clean() {
        this.records = new Vector3f[0];
        return super.clean();
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putVector3(this.position);
        this.putVarInt((int) (this.radius * 32));

        this.putUnsignedVarInt(this.records.length);
        for (Vector3f record : records) {
            this.putSignedBlockPosition(record.asBlockVector3());
        }
    }
}
