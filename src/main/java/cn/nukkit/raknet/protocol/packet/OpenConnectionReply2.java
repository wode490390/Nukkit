package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.Packet;

import java.net.InetSocketAddress;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class OpenConnectionReply2 extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_OPEN_CONNECTION_REPLY_2;

    @Override
    public byte getID() {
        return ID;
    }

    public long serverID;
    public String clientAddress;
    public int clientPort;
    public short mtuSize;
    public boolean serverSecurity = false;

    @Override
    public void encode() {
        super.encode();
        this.writeMagic();
        this.putLong(this.serverID);
        this.putAddress(this.clientAddress, this.clientPort);
        this.putShort(this.mtuSize);
        this.putByte((byte) (this.serverSecurity ? 1 : 0));
    }

    @Override
    public void decode() {
        super.decode();
        this.readMagic();
        this.serverID = this.getLong();
        InetSocketAddress address = this.getAddress();
        this.clientAddress = address.getHostString();
        this.clientPort = address.getPort();
        this.mtuSize = (short) this.getShort();
        this.serverSecurity = (this.getByte() != 0);
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new OpenConnectionReply2();
        }

    }
}
