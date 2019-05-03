package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class PhotoTransferPacket extends DataPacket implements ClientboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PHOTO_TRANSFER_PACKET;

    public String photoName;
    public byte[] photoData;
    public String bookId; //photos are stored in a sibling directory to the games folder (screenshots/(some UUID)/bookID/example.png)

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
        this.putString(this.photoName);
        this.putUnsignedVarInt(this.photoData.length);
        this.put(this.photoData);
        this.putString(this.bookId);
    }
}
