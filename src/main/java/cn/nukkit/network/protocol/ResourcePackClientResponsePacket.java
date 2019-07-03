package cn.nukkit.network.protocol;

import java.util.UUID;
import lombok.ToString;

@ToString
public class ResourcePackClientResponsePacket extends DataPacket implements ServerboundPacket {

    public static final byte NETWORK_ID = ProtocolInfo.RESOURCE_PACK_CLIENT_RESPONSE_PACKET;

    public static final byte STATUS_REFUSED = 1;
    public static final byte STATUS_SEND_PACKS = 2;
    public static final byte STATUS_HAVE_ALL_PACKS = 3;
    public static final byte STATUS_COMPLETED = 4;

    public byte status;
    public Entry[] packIds = new Entry[0];

    @Override
    public void decode() {
        this.status = (byte) this.getByte();
        this.packIds = new Entry[this.getLShort()];
        for (int i = 0; i < this.packIds.length; i++) {
            String[] entry = this.getString().split("_");
            this.packIds[i] = new Entry(UUID.fromString(entry[0]), entry[1]);
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(this.status);
        this.putLShort(this.packIds.length);
        for (Entry entry : this.packIds) {
            this.putString(entry.uuid.toString() + '_' + entry.version);
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public static class Entry {
        public final UUID uuid;
        public final String version;

        public Entry(UUID uuid, String version) {
            this.uuid = uuid;
            this.version = version;
        }
    }
}
