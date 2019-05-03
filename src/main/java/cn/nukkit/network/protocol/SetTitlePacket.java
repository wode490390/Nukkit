package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * @author Tee7even
 */
@ToString
public class SetTitlePacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_TITLE_PACKET;

    public static final int TYPE_CLEAR_TITLE = 0;
    public static final int TYPE_RESET_TITLE = 1;
    public static final int TYPE_SET_TITLE = 2;
    public static final int TYPE_SET_SUBTITLE = 3;
    public static final int TYPE_SET_ACTIONBAR_MESSAGE = 4;
    public static final int TYPE_SET_ANIMATION_TIMES = 5;

    public int type;
    public String text = "";
    public int fadeInTime = 0;
    public int stayTime = 0;
    public int fadeOutTime = 0;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.type = this.getVarInt();
        this.text = this.getString();
        this.fadeInTime = this.getVarInt();
        this.stayTime = this.getVarInt();
        this.fadeOutTime = this.getVarInt();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(this.type);
        this.putString(this.text);
        this.putVarInt(this.fadeInTime);
        this.putVarInt(this.stayTime);
        this.putVarInt(this.fadeOutTime);
    }
}
