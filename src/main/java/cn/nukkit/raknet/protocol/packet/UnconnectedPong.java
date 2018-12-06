package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class UnconnectedPong extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_UNCONNECTED_PONG;

    @Override
    public byte getID() {
        return ID;
    }

    public long pingID;
    public long serverID;
    public String serverName;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
        this.putLong(this.serverID);
        this.writeMagic();
        this.putString(this.serverName);
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
        this.serverID = this.getLong();
        this.readMagic();
        this.serverName = this.getString();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new UnconnectedPong();
        }

    }
}
