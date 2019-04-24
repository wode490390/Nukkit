package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class UpdateSoftEnumPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_SOFT_ENUM_PACKET;

    public static final int TYPE_ADD = 0;
    public static final int TYPE_REMOVE = 1;
    public static final int TYPE_SET = 2;

    public String enumName = "";
    public String[] values = new String[0];
    public int type = TYPE_SET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putString(this.enumName);
        this.putUnsignedVarInt(this.values.length);
        for (String value : this.values) {
            this.putString(value);
        }
        this.putByte((byte) this.type);
    }
}
