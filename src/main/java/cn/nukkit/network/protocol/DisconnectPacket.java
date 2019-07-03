package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * Created by on 15-10-12.
 */
@ToString
public class DisconnectPacket extends DataPacket implements ClientboundPacket, ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.DISCONNECT_PACKET;

    public boolean hideDisconnectionScreen = false;
    public String message = "";

    @Override
    public boolean canBeSentBeforeLogin() {
        return true;
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.hideDisconnectionScreen = this.getBoolean();
        if (!this.hideDisconnectionScreen) {
            this.message = this.getString();
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putBoolean(this.hideDisconnectionScreen);
        if (!this.hideDisconnectionScreen) {
            this.putString(this.message);
        }
    }
}
