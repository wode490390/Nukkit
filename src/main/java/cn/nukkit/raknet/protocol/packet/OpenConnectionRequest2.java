package cn.nukkit.raknet.protocol.packet;

import cn.nukkit.raknet.protocol.MessageIdentifiers;
import cn.nukkit.raknet.protocol.OfflineMessage;
import cn.nukkit.raknet.protocol.Packet;

import java.net.InetSocketAddress;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class OpenConnectionRequest2 extends OfflineMessage {

    public static final byte ID = MessageIdentifiers.ID_OPEN_CONNECTION_REQUEST_2;

    @Override
    public byte getID() {
        return ID;
    }

    public long clientID;
    public String serverAddress;
    public int serverPort;
    public short mtuSize;

    @Override
    public void encode() {
        super.encode();
        this.writeMagic();
        this.putAddress(this.serverAddress, this.serverPort);
        this.putShort(this.mtuSize);
        this.putLong(this.clientID);
    }

    @Override
    public void decode() {
        super.decode();
        this.readMagic();
        InetSocketAddress address = this.getAddress();
        this.serverAddress = address.getHostString();
        this.serverPort = address.getPort();
        this.mtuSize = this.getShort();
        this.clientID = this.getLong();
    }

    public static final class Factory implements Packet.PacketFactory {

        @Override
        public Packet create() {
            return new OpenConnectionRequest2();
        }

    }
}
