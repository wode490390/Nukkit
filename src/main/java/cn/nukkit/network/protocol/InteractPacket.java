package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * Created on 15-10-15.
 */
@ToString
public class InteractPacket extends DataPacket implements ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INTERACT_PACKET;

    public static final int ACTION_UNKNOWN_1 = 1;
    public static final int ACTION_DAMAGE = 2;
    public static final int ACTION_VEHICLE_EXIT = 3;
    public static final int ACTION_MOUSEOVER = 4;
    public static final int ACTION_UNKNOWN_5 = 5;
    public static final int ACTION_OPEN_INVENTORY = 6;

    public int action;
    public long target;

    public float x;
    public float y;
    public float z;

    @Override
    public void decode() {
        this.action = this.getByte();
        this.target = this.getEntityRuntimeId();

        if (this.action == ACTION_MOUSEOVER) {
            //TODO: should this be a vector3?
            this.x = this.getLFloat();
            this.y = this.getLFloat();
            this.z = this.getLFloat();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.action);
        this.putEntityRuntimeId(this.target);

        if (this.action == ACTION_MOUSEOVER) {
            this.putLFloat(this.x);
            this.putLFloat(this.y);
            this.putLFloat(this.z);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
