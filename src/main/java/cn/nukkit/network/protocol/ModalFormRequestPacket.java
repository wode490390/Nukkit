package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ModalFormRequestPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MODAL_FORM_REQUEST_PACKET;

    public int formId;
    public String formData; //json

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
        this.putUnsignedVarInt(this.formId);
        this.putString(this.formData);
    }
}
