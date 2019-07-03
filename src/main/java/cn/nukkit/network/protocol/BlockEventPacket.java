package cn.nukkit.network.protocol;

import lombok.ToString;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@ToString
public class BlockEventPacket extends DataPacket implements ClientboundPacket {

    public static final int TYPE_CHEST = 1;

    public static final int DATA_CHEST_CLOSE = 0;
    public static final int DATA_CHEST_OPEN = 2;

    public static final int TYPE_ENDGATEWAY = 1;
    //public static final int DATA_ENDGATEWAY_COOLDOWN = 0;

    public static final int TYPE_NOTEBLOCK_PIANO = 0;
    public static final int TYPE_NOTEBLOCK_BASEDRUM = 1;
    public static final int TYPE_NOTEBLOCK_STICKS = 2;
    public static final int TYPE_NOTEBLOCK_DRUM = 3;
    public static final int TYPE_NOTEBLOCK_BASS = 4;

    public static final int DATA_NOTEBLOCK_PITCH0 = 0;
    public static final int DATA_NOTEBLOCK_PITCH1 = 1;
    public static final int DATA_NOTEBLOCK_PITCH2 = 2;
    public static final int DATA_NOTEBLOCK_PITCH3 = 3;
    public static final int DATA_NOTEBLOCK_PITCH4 = 4;
    public static final int DATA_NOTEBLOCK_PITCH5 = 5;
    public static final int DATA_NOTEBLOCK_PITCH6 = 6;
    public static final int DATA_NOTEBLOCK_PITCH7 = 7;
    public static final int DATA_NOTEBLOCK_PITCH8 = 8;
    public static final int DATA_NOTEBLOCK_PITCH9 = 9;
    public static final int DATA_NOTEBLOCK_PITCH10 = 10;
    public static final int DATA_NOTEBLOCK_PITCH11 = 11;
    public static final int DATA_NOTEBLOCK_PITCH12 = 12;
    public static final int DATA_NOTEBLOCK_PITCH13 = 13;
    public static final int DATA_NOTEBLOCK_PITCH14 = 14;
    public static final int DATA_NOTEBLOCK_PITCH15 = 15;
    public static final int DATA_NOTEBLOCK_PITCH16 = 16;
    public static final int DATA_NOTEBLOCK_PITCH17 = 17;
    public static final int DATA_NOTEBLOCK_PITCH18 = 18;
    public static final int DATA_NOTEBLOCK_PITCH19 = 19;
    public static final int DATA_NOTEBLOCK_PITCH20 = 20;
    public static final int DATA_NOTEBLOCK_PITCH21 = 21;
    public static final int DATA_NOTEBLOCK_PITCH22 = 22;
    public static final int DATA_NOTEBLOCK_PITCH23 = 23;
    public static final int DATA_NOTEBLOCK_PITCH24 = 24;

    public static final byte NETWORK_ID = ProtocolInfo.BLOCK_EVENT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int x;
    public int y;
    public int z;
    public int eventType;
    public int eventData;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putBlockPosition(this.x, this.y, this.z);
        this.putVarInt(this.eventType);
        this.putVarInt(this.eventData);
    }
}
