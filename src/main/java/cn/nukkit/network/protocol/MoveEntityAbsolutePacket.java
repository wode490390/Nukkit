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

    public long entityRuntimeId = 0;
    public int flags = 0;
    public Vector3f position = new Vector3f();
    public double xRot = 0.0d;
    public double yRot = 0.0d;
    public double zRot = 0.0d;

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
        this.putByte((byte) this.flags);
        this.putVector3(this.position);
        this.putByteRotation((float) this.xRot);
        this.putByteRotation((float) this.yRot);
        this.putByteRotation((float) this.zRot);
    }
}
