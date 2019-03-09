package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class OpenConnectionReply1 extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_OPEN_CONNECTION_REPLY_1;

    @Override
    public byte getID() {
        return ID;
    }

    public long serverID;
    public boolean serverSecurity = false;
    public short mtuSize;

    @Override
    public void encode() {
        super.encode();
        this.writeMagic();
        this.putLong(this.serverID);
        this.putByte((byte) (this.serverSecurity ? 1 : 0));
        this.putShort(this.mtuSize);
    }

    @Override
    public void decode() {
        super.decode();
        this.readMagic();
        this.serverID = this.getLong();
        this.serverSecurity = (this.getByte() != 0);
        this.mtuSize = (short) this.getShort();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new OpenConnectionReply1();
        }

    }
}
