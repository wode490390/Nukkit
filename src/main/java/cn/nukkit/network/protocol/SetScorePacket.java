package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.ScorePacketEntry;

public class SetScorePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_SCORE_PACKET;

    public static final int TYPE_CHANGE = 0;
    public static final int TYPE_REMOVE = 1;

    public int type;
    public ScorePacketEntry[] entries;

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
        this.putByte((byte) this.type);
        this.putUnsignedVarInt(this.entries.length);
        for (ScorePacketEntry entry : entries) {
            this.putByte((byte) entry.type);
            switch (entry.type) {
                case ScorePacketEntry.TYPE_PLAYER:
                case ScorePacketEntry.TYPE_ENTITY:
                    this.putEntityUniqueId(entry.entityUniqueId);
                    break;
                case ScorePacketEntry.TYPE_FAKE_PLAYER:
                    this.putString(entry.customName);
                    break;
                default:
                    throw new Exception("Unknown entry type " + entry.type);
            }
        }
    }

}