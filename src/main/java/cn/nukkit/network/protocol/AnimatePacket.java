package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Nukkit Project Team
 */
@ToString
public class AnimatePacket extends DataPacket implements ClientboundPacket, ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ANIMATE_PACKET;

    public static final int ACTION_NO_ACTION = 0;
    public static final int ACTION_SWING_ARM = 1;

    public static final int ACTION_STOP_SLEEP = 3;
    public static final int ACTION_CRITICAL_HIT = 4;
    public static final int ACTION_MAGIC_CRITICAL_HIT = 5;

    public static final int ACTION_ROW_RIGHT = 128;
    public static final int ACTION_ROW_LEFT = 129;

    public int action;
    public long entityRuntimeId;
    public float rowingTime = 0;

    @Override
    public void decode() {
        this.action = this.getVarInt();
        this.entityRuntimeId = getEntityRuntimeId();
        if (this.action == ACTION_ROW_LEFT || this.action == ACTION_ROW_RIGHT) {
            this.rowingTime = this.getLFloat();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.action);
        this.putEntityRuntimeId(this.entityRuntimeId);
        if (this.action == ACTION_ROW_LEFT || this.action == ACTION_ROW_RIGHT) {
            this.putLFloat(this.rowingTime);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
