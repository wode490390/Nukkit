package cn.nukkit.network.protocol;

public class PhotoTransferPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PHOTO_TRANSFER_PACKET;

    public String photoName;
    public String photoData;
    public String bookId;

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
        this.putString(this.photoData);
        this.putString(this.bookId);
    }

}