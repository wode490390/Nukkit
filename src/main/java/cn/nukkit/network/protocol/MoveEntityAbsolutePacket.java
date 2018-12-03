package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class MoveEntityAbsolutePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOVE_ENTITY_ABSOLUTE_PACKET;

    public static final int FLAG_GROUND = 0x01;
    public static final int FLAG_TELEPORT = 0x02;

    public long entityRuntimeId;
    public int flags = 0;
    public Vector3f position;
    public double xRot;
    public double yRot;
    public double zRot;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.flags = this.getByte();
        this.position = this.getVector3();
        this.xRot = this.getByteRotation();
        this.yRot = this.getByteRotation();
        this.zRot = this.getByteRotation();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putByte(this.flags);
        this.putVector3(this.position);
        this.putByteRotation(this.xRot);
        this.putByteRotation(this.yRot);
        this.putByteRotation(this.zRot);
    }
}
