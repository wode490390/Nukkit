package cn.nukkit.network.protocol;

/**
 * @author Nukkit Project Team
 */
public class AddPaintingPacket extends AddHangingEntityPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ADD_PAINTING_PACKET;

    public String title;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        super.encode();
        this.putString(this.title);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
