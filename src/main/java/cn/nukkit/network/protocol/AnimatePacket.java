package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class AnimatePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ANIMATE_PACKET;

    public static final int ACTION_SWING_ARM = 1;

    public static final int ACTION_STOP_SLEEP = 3;
    public static final int ACTION_CRITICAL_HIT = 4;

    public int action;
    public long entityRuntimeId;
    public float unknown = 0.0; //TODO (Boat rowing time?)

    @Override
    public void decode() {
        this.action = this.getVarInt();
        this.entityRuntimeId = getEntityRuntimeId();
        if ((this.action & 0x80) != 0) {
            this.unknown = this.getLFloat();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.action);
        this.putEntityRuntimeId(this.entityRuntimeId);
        if ((this.action & 0x80) != 0) {
            this.putLFloat(this.unknown);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
