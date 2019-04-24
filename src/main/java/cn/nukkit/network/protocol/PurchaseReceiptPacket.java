package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class PurchaseReceiptPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PURCHASE_RECEIPT_PACKET;

    public String[] entries = new String[0];

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
        this.putUnsignedVarInt(this.entries.length);
        for (String entry : entries) {
            this.putString(entry);
        }
    }
}
