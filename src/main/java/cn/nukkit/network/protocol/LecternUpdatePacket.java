package cn.nukkit.network.protocol;

import cn.nukkit.math.BlockVector3;
import lombok.ToString;

@ToString
public class LecternUpdatePacket extends DataPacket implements ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.LECTERN_UPDATE_PACKET;

    public int page;
    public int totalPages;
    public int x;
    public int y;
    public int z;
    public boolean dropBook;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.page = this.getByte();
        this.totalPages = this.getByte();
        BlockVector3 v = this.getBlockPosition();
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.dropBook = this.getBoolean();
    }

    @Override
    public void encode() {

    }
}
