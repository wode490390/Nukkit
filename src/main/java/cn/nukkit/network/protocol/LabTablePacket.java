package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;

public class LabTablePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.LAB_TABLE_PACKET;

    public byte uselessByte; //0 for client -> server, 1 for server -> client. Seems useless.

    public int x;
    public int y;
    public int z;

    public int reactionType;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.uselessByte = (byte) this.getByte();
        BlockVector3 vector = this.getSignedBlockPosition();
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.reactionType = this.getByte();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.uselessByte);
        this.putSignedBlockPosition(new BlockVector3(this.x, this.y, this.z));
        this.putByte((byte) this.reactionType);
    }
}