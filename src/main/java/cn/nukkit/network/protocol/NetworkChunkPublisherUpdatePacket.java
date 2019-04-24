package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import lombok.ToString;

@ToString
public class NetworkChunkPublisherUpdatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.NETWORK_CHUNK_PUBLISHER_UPDATE_PACKET;

    public int x;
    public int y;
    public int z;
    public int radius;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        BlockVector3 v = this.getSignedBlockPosition();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.radius = (int) this.getUnsignedVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putSignedBlockPosition(new BlockVector3(this.x, this.y, this.z));
        this.putUnsignedVarInt(radius);
    }
}
