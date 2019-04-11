package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;

public class BlockPickRequestPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BLOCK_PICK_REQUEST_PACKET;

    public int blockX;
    public int blockY;
    public int blockZ;
    public boolean addUserData = false;
    public int hotbarSlot;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        BlockVector3 v = this.getSignedBlockPosition();
        this.blockX = v.x;
        this.blockY = v.y;
        this.blockZ = v.z;
        this.addUserData = this.getBoolean();
        this.hotbarSlot = this.getByte();
    }

    @Override
    public void encode() {

    }
}
