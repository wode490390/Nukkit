package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3;

/**
 * Created on 15-10-14.
 */
public class MovePlayerPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOVE_PLAYER_PACKET;

    public static final int MODE_NORMAL = 0;
    public static final int MODE_RESET = 1;
    public static final int MODE_TELEPORT = 2;
    public static final int MODE_PITCH = 3; //facepalm Mojang

    public long entityRuntimeId;
    public Vector3 position;
    public float pitch;
    public float yaw;
    public float headYaw;
    public int mode = MODE_NORMAL;
    public boolean onGround = false;
    public long ridingEid = 0;
    public int teleportCause = 0;
    public int teleportItem = 0;

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.position = this.getVector3().asVector3();
        this.pitch = this.getLFloat();
        this.yaw = this.getLFloat();
        this.headYaw = this.getLFloat();
        this.mode = this.getByte();
        this.onGround = this.getBoolean();
        this.ridingEid = this.getEntityRuntimeId();
        if (this.mode == MODE_TELEPORT) {
            this.teleportCause = this.getLInt();
            this.teleportItem = this.getLInt();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putVector3(this.position.asVector3f());
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        this.putLFloat(this.headYaw);
        this.putByte((byte) this.mode);
        this.putBoolean(this.onGround);
        this.putEntityRuntimeId(this.ridingEid);
        if (this.mode == MODE_TELEPORT) {
            this.putLInt(this.teleportCause);
            this.putLInt(this.teleportItem);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
