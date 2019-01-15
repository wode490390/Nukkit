package cn.nukkit.network.protocol;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class UpdateBlockPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_BLOCK_PACKET;

    public static final int FLAG_NONE = 0b0000;
    public static final int FLAG_NEIGHBORS = 0b0001;
    public static final int FLAG_NETWORK = 0b0010;
    public static final int FLAG_NOGRAPHIC = 0b0100;
    public static final int FLAG_PRIORITY = 0b1000;

    public static final int FLAG_ALL = (FLAG_NEIGHBORS | FLAG_NETWORK);
    public static final int FLAG_ALL_PRIORITY = (FLAG_ALL | FLAG_PRIORITY);

    public static final int DATA_LAYER_NORMAL = 0;
    public static final int DATA_LAYER_LIQUID = 1;

    public int x;
    public int z;
    public int y;
    public int blockRuntimeId;
    public int flags = 0x02;
    public int dataLayerId = DATA_LAYER_NORMAL;

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
        this.putBlockPosition(this.x, this.y, this.z);
        this.putUnsignedVarInt(this.blockRuntimeId);
        this.putUnsignedVarInt(this.flags);
        this.putUnsignedVarInt(this.dataLayerId);
    }

    public static class Entry {
        public final int x;
        public final int z;
        public final int y;
        public final int blockId;
        public final int blockData;
        public final int flags;

        public Entry(int x, int z, int y, int blockId, int blockData, int flags) {
            this.x = x;
            this.z = z;
            this.y = y;
            this.blockId = blockId;
            this.blockData = blockData;
            this.flags = flags;
        }
    }
}
