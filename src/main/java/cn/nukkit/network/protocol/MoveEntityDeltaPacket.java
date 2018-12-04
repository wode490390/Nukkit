package cn.nukkit.network.protocol;

public class MoveEntityDeltaPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MOVE_ENTITY_DELTA_PACKET;

    public static final int FLAG_HAS_X = 0b1;
    public static final int FLAG_HAS_Y = 0b10;
    public static final int FLAG_HAS_Z = 0b100;
    public static final int FLAG_HAS_ROT_X = 0b1000;
    public static final int FLAG_HAS_ROT_Y = 0b1010;
    public static final int FLAG_HAS_ROT_Z = 0b10100;

    public long entityRuntimeId;
    public int flags;
    public int xDiff = 0;
    public int yDiff = 0;
    public int zDiff = 0;
    public double xRot = 0d;
    public double yRot = 0d;
    public double zRot = 0d;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.entityRuntimeId = this.getEntityRuntimeId();
        this.flags = this.getByte();
        this.xDiff = this.maybeReadCoord(FLAG_HAS_X);
        this.yDiff = this.maybeReadCoord(FLAG_HAS_Y);
        this.zDiff = this.maybeReadCoord(FLAG_HAS_Z);
        this.xRot = this.maybeReadRotation(FLAG_HAS_ROT_X);
        this.yRot = this.maybeReadRotation(FLAG_HAS_ROT_Y);
        this.zRot = this.maybeReadRotation(FLAG_HAS_ROT_Z);
    }

    @Override
    public void encode() {
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putByte((byte) flags);
        this.maybeWriteCoord(FLAG_HAS_X, this.xDiff);
        this.maybeWriteCoord(FLAG_HAS_Y, this.yDiff);
        this.maybeWriteCoord(FLAG_HAS_Z, this.zDiff);
        this.maybeWriteRotation(FLAG_HAS_ROT_X, this.xRot);
        this.maybeWriteRotation(FLAG_HAS_ROT_Y, this.yRot);
        this.maybeWriteRotation(FLAG_HAS_ROT_Z, this.zRot);
    }

    private int maybeReadCoord(int flag) {
        if ((this.flags & flag) != 0) {
            return this.getVarInt();
        }
        return 0;
    }

    private double maybeReadRotation(int flag) {
        if ((this.flags & flag) != 0) {
            return this.getByteRotation();
        }
        return 0d;
    }

    private void maybeWriteCoord(int flag, int val) {
        if ((this.flags & flag) != 0) {
            this.putVarInt(val);
        }
    }

    private void maybeWriteRotation(int flag, double val) {
        if ((this.flags & flag) != 0) {
            this.putByteRotation((float) val);
        }
    }
}
