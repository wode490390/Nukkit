package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ModalFormResponsePacket extends DataPacket implements ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MODAL_FORM_RESPONSE_PACKET;

    public int formId;
    public String formData; //json

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.formId = (int) this.getUnsignedVarInt();
        this.formData = this.getString(); //Data will be null if player close form without submit (by cross button or ESC)
    }

    @Override
    public void encode() {

    }
}
