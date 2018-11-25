package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.ScorePacketEntry;

import java.util.ArrayList;
import java.util.List;

public class SetScorePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.SET_SCORE_PACKET;

    public static final int TYPE_CHANGE = 0;
    public static final int TYPE_REMOVE = 1;

    public int type;
    public List<ScorePacketEntry> entries = new ArrayList<ScorePacketEntry>();

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
        this.putUnsignedVarInt(this.entries.size());
        for (ScorePacketEntry entry : entries) {
            switch (entry.type) {
                case ScorePacketEntry.TYPE_PLAYER:
                case ScorePacketEntry.TYPE_ENTITY:
                    this.putByte((byte) entry.type);
                    this.putEntityUniqueId(entry.entityUniqueId);
                    break;
                case ScorePacketEntry.TYPE_FAKE_PLAYER:
                    this.putByte((byte) entry.type);
                    this.putString(entry.customName);
                    break;
                default:
                    this.putByte((byte) ScorePacketEntry.TYPE_FAKE_PLAYER);
                    this.putString("Unknown type " + entry.type);
                    break;
            }
        }
    }
}
